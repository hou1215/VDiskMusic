package com.hyuan.diy.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.hyuan.diy.utils.Constant;
import com.vdisk.android.VDiskAuthSession;
import com.vdisk.android.VDiskDialogListener;
import com.vdisk.net.exception.VDiskDialogError;
import com.vdisk.net.exception.VDiskException;
import com.vdisk.net.exception.VDiskServerException;
import com.vdisk.net.session.AccessToken;
import com.vdisk.net.session.AppKeyPair;
import com.vdisk.net.session.Session.AccessType;

public class VDiskActivity extends Activity implements VDiskDialogListener{

	/**
	 * 替换为开发者应用的appkey，例如"16*****960";
	 * 
	 * Replace it to the appkey of developer's application, such as
	 * "16*****960";
	 */
	public static final String CONSUMER_KEY = "2410222318";// TODO

	/**
	 * 替换为开发者应用的app secret，例如"94098*****************861f9";
	 * 
	 * Replace it to the app secret of developer's application, such as
	 * "94098*****************861f9";
	 */
	public static final String CONSUMER_SECRET = "23c1237daa1da80912ef85916a5b70dc";// TODO

	/**
	 * 替换为微博的access_token. 如果你想使用微博token直接访问微盘的API，这个字段不能为空。
	 * 
	 * Replace it to the access_token of WEIBO. If you use weibo token to access
	 * VDisk API, this field should not be null.
	 */
	public static String WEIBO_ACCESS_TOKEN = 
			"http://vdisk.weibo.com/developers/index.php?module=appmanager&action=editapp&appid=2410222318";

	/**
	 * 
	 * 此处应该替换为与appkey对应的应用回调地址，对应的应用回调地址可在开发者登陆新浪微盘开发平台之后，进入"我的应用--编辑应用信息--回调地址"
	 * 进行设置和查看，如果使用微盘token登陆的话， 应用回调页不可为空。
	 * 
	 * The content of this field should replace with the application's redirect
	 * url of corresponding appkey. Developers can login in Sina VDisk
	 * Development Platform and enter "我的应用--编辑应用信息--回调地址" to set and view the
	 * corresponding application's redirect url. If you use VDisk token, the
	 * redirect url should not be empty. should not be empty.
	 */
	private static final String REDIRECT_URL = "http://vdisk.weibo.com/developers/index.php?";// TODO

	private AccessToken mVDiskAccessToken;
	private ProgressDialog dialog;

	private static final int SUCCESS = 0;
	private static final int FAILED = -1;
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {

			Bundle values = msg.getData();
			switch (msg.what) {
			case SUCCESS: {
				String error = values.getString("error");
				String error_code = values.getString("error_code");

				if (error == null && error_code == null) {
					VDiskActivity.this.onComplete(values);
					Constant.session.updateOAuth2Preference(VDiskActivity.this,
							mVDiskAccessToken);
				} else if (error.equals("access_denied")) {
					// 用户或授权服务器拒绝授予数据访问权限
					// User or authorization server refuses to grant data access
					// permission
					VDiskActivity.this.onCancel();
				} else {
					VDiskActivity.this
							.onVDiskException(new VDiskServerException(error,
									Integer.parseInt(error_code)));
				}
				dialog.dismiss();
			}
				break;
			case FAILED: {
				VDiskException e = (VDiskException) values
						.getSerializable("error");
				VDiskActivity.this.onVDiskException(e);
				dialog.dismiss();
			}
				break;
			default:
				break;
			}

		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/**
		 * 初始化 Init
		 */
		AppKeyPair appKeyPair = new AppKeyPair(CONSUMER_KEY, CONSUMER_SECRET);
		/**
		 * @AccessType.APP_FOLDER - sandbox 模式
		 * @AccessType.VDISK - basic 模式
		 */
		Constant.session = VDiskAuthSession.getInstance(this, appKeyPair,
				AccessType.APP_FOLDER);

		
		try 
		{
			mVDiskAccessToken = VDiskAuthSession.getOAuth2Preference(this,
					appKeyPair);
			
			if (!Constant.session.isLinked()) {
				// 使用微盘Token认证，需设置重定向网址
				// Need to set REDIRECT_URL if you want to use VDisk token.
				Constant.session.setRedirectUrl(REDIRECT_URL);
				Constant.session.authorize(VDiskActivity.this, VDiskActivity.this);
			} 
			
			// 如果已经登录，直接跳转到测试页面
			// If you are already logged in, jump to the test page directly.
			if (Constant.session.isLinked()) {
				finish();
			} 
			else if (!TextUtils.isEmpty(mVDiskAccessToken.mRefreshToken)
					&& !VDiskAuthSession.isSessionValid(mVDiskAccessToken)) 
			{
				// 如果微盘AccessToken过期，使用RefreshToken刷新AccessToken并登录
				// If VDisk AccessToken has expired ,use RefreshToken to refresh the
				// VDisk AccessToken then login.
				
				dialog = new ProgressDialog(this);
				dialog.setMessage("正在加载，请稍候…");

				refreshLogin();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * 使用RefreshToken刷新AccessToken并登录
	 * 
	 * Use RefreshToken to refresh the VDisk AccessToken then login.
	 * 
	 */
	private void refreshLogin() {
		dialog.show();
		new Thread() {

			public void run() {
				Message msg = new Message();
				try {
					mVDiskAccessToken = Constant.session
							.refreshOAuth2AccessToken(
									mVDiskAccessToken.mRefreshToken,
									VDiskActivity.this);
				} catch (VDiskException e) {
					msg.what = FAILED;
					Bundle error = new Bundle();
					error.putSerializable("error", e);
					msg.setData(error);
					handler.sendMessage(msg);
					return;
				}
				Bundle values = new Bundle();
				values.putSerializable(VDiskAuthSession.OAUTH2_TOKEN,
						mVDiskAccessToken);
				msg.setData(values);
				msg.what = SUCCESS;
				handler.sendMessage(msg);
			};
		}.start();
	}

	/**
	 * 认证结束后的回调方法
	 * 
	 * Callback method after authentication.
	 */
	@Override
	public void onComplete(Bundle values) {

		if (values != null) {
			AccessToken mToken = (AccessToken) values
					.getSerializable(VDiskAuthSession.OAUTH2_TOKEN);
			Constant.session.finishAuthorize(mToken);
		}

		finish();
	}

	/**
	 * 认证出错的回调方法
	 * 
	 * Callback method for authentication errors.
	 */
	@Override
	public void onError(VDiskDialogError error) {
		Toast.makeText(getApplicationContext(),
				"Auth error : " + error.getMessage(), Toast.LENGTH_LONG).show();
	}

	/**
	 * 认证异常的回调方法
	 * 
	 * Callback method for authentication exceptions.
	 */
	@Override
	public void onVDiskException(VDiskException exception) {
		Toast.makeText(getApplicationContext(),
				"Auth exception : " + exception.getMessage(), Toast.LENGTH_LONG)
				.show();
	}

	/**
	 * 认证被取消的回调方法
	 * 
	 * Callback method as authentication is canceled.
	 */
	@Override
	public void onCancel() {
		Toast.makeText(getApplicationContext(), "Auth cancel",
				Toast.LENGTH_LONG).show();
	}

}

