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
	 * �滻Ϊ������Ӧ�õ�appkey������"16*****960";
	 * 
	 * Replace it to the appkey of developer's application, such as
	 * "16*****960";
	 */
	public static final String CONSUMER_KEY = "2410222318";// TODO

	/**
	 * �滻Ϊ������Ӧ�õ�app secret������"94098*****************861f9";
	 * 
	 * Replace it to the app secret of developer's application, such as
	 * "94098*****************861f9";
	 */
	public static final String CONSUMER_SECRET = "23c1237daa1da80912ef85916a5b70dc";// TODO

	/**
	 * �滻Ϊ΢����access_token. �������ʹ��΢��tokenֱ�ӷ���΢�̵�API������ֶβ���Ϊ�ա�
	 * 
	 * Replace it to the access_token of WEIBO. If you use weibo token to access
	 * VDisk API, this field should not be null.
	 */
	public static String WEIBO_ACCESS_TOKEN = 
			"http://vdisk.weibo.com/developers/index.php?module=appmanager&action=editapp&appid=2410222318";

	/**
	 * 
	 * �˴�Ӧ���滻Ϊ��appkey��Ӧ��Ӧ�ûص���ַ����Ӧ��Ӧ�ûص���ַ���ڿ����ߵ�½����΢�̿���ƽ̨֮�󣬽���"�ҵ�Ӧ��--�༭Ӧ����Ϣ--�ص���ַ"
	 * �������úͲ鿴�����ʹ��΢��token��½�Ļ��� Ӧ�ûص�ҳ����Ϊ�ա�
	 * 
	 * The content of this field should replace with the application's redirect
	 * url of corresponding appkey. Developers can login in Sina VDisk
	 * Development Platform and enter "�ҵ�Ӧ��--�༭Ӧ����Ϣ--�ص���ַ" to set and view the
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
					// �û�����Ȩ�������ܾ��������ݷ���Ȩ��
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
		 * ��ʼ�� Init
		 */
		AppKeyPair appKeyPair = new AppKeyPair(CONSUMER_KEY, CONSUMER_SECRET);
		/**
		 * @AccessType.APP_FOLDER - sandbox ģʽ
		 * @AccessType.VDISK - basic ģʽ
		 */
		Constant.session = VDiskAuthSession.getInstance(this, appKeyPair,
				AccessType.APP_FOLDER);

		
		try 
		{
			mVDiskAccessToken = VDiskAuthSession.getOAuth2Preference(this,
					appKeyPair);
			
			if (!Constant.session.isLinked()) {
				// ʹ��΢��Token��֤���������ض�����ַ
				// Need to set REDIRECT_URL if you want to use VDisk token.
				Constant.session.setRedirectUrl(REDIRECT_URL);
				Constant.session.authorize(VDiskActivity.this, VDiskActivity.this);
			} 
			
			// ����Ѿ���¼��ֱ����ת������ҳ��
			// If you are already logged in, jump to the test page directly.
			if (Constant.session.isLinked()) {
				finish();
			} 
			else if (!TextUtils.isEmpty(mVDiskAccessToken.mRefreshToken)
					&& !VDiskAuthSession.isSessionValid(mVDiskAccessToken)) 
			{
				// ���΢��AccessToken���ڣ�ʹ��RefreshTokenˢ��AccessToken����¼
				// If VDisk AccessToken has expired ,use RefreshToken to refresh the
				// VDisk AccessToken then login.
				
				dialog = new ProgressDialog(this);
				dialog.setMessage("���ڼ��أ����Ժ�");

				refreshLogin();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * 
	 * ʹ��RefreshTokenˢ��AccessToken����¼
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
	 * ��֤������Ļص�����
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
	 * ��֤����Ļص�����
	 * 
	 * Callback method for authentication errors.
	 */
	@Override
	public void onError(VDiskDialogError error) {
		Toast.makeText(getApplicationContext(),
				"Auth error : " + error.getMessage(), Toast.LENGTH_LONG).show();
	}

	/**
	 * ��֤�쳣�Ļص�����
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
	 * ��֤��ȡ���Ļص�����
	 * 
	 * Callback method as authentication is canceled.
	 */
	@Override
	public void onCancel() {
		Toast.makeText(getApplicationContext(), "Auth cancel",
				Toast.LENGTH_LONG).show();
	}

}

