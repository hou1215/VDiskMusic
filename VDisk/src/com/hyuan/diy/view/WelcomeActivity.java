package com.hyuan.diy.view;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import com.hyuan.diy.R;
import com.hyuan.diy.biz.MusicBiz;
import com.hyuan.diy.myapplication.MyApplicaotion;
import com.hyuan.diy.utils.BitmapUtil;
import com.hyuan.diy.utils.Constant;
import com.hyuan.diy.utils.LoadCollectionDb;

public class WelcomeActivity extends Activity {


	private int[] 		images = {R.drawable.b01,R.drawable.b02,R.drawable.b03,
								R.drawable.b04,R.drawable.b05,R.drawable.b06};
	Handler      	   handler = new Handler();
	private ImageView  iv_cd;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		initView();
		LoadMusics();
		loadDAta();
		setRotate();
		
		
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
				finish();
			}
		}, 3000);
	}
	
	private void setRotate(){
		RotateAnimation an = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);  
        an.setInterpolator(new LinearInterpolator());//不停顿  
        an.setRepeatCount(100);//重复次数  
        an.setFillAfter(true);//停在最后  
        an.setDuration(4000); 
        iv_cd.startAnimation(an);
	}
	
	private void initView() {
		//iv_anim = (ImageView) findViewById(R.id.iv_anim);
		iv_cd = (ImageView) findViewById(R.id.iv_cd);
		
		
	}
	private void LoadMusics() {
		LoadThread thread = new LoadThread();
		thread.start();
		
	}
	
	public class LoadThread extends Thread{

		@Override
		public void run() {
			MusicBiz.loadmusics(WelcomeActivity.this);
			Cursor cursor = MyApplicaotion.collectDB.query("musicsColleck", null, null, null);
	        LoadCollectionDb.tracks(cursor);
	        
	        for (int i = 0; i < Constant.collectDb.size(); i++) {
	        	Constant.collectlist.add(Constant.collectDb.get(i));
	        	Constant.collectUrl.add(Constant.collectDb.get(i).getData());
			}
	        Constant.collectDb.clear();
	        
			super.run();
		}
	}

	private void loadDAta() {
		new Thread(){
			public void run() {
				for (int id : images)
		        {
		            Bitmap bitmap = createReflectedBitmapById(id);
		            if (null != bitmap)
		            {
		                BitmapDrawable drawable = new BitmapDrawable(bitmap);
		                drawable.setAntiAlias(true);
		                Constant.mBitmaps.add(drawable);
		            }
		        }
			};
		}.start();
		
	}
	private Bitmap createReflectedBitmapById(int resId)
    {
        Drawable drawable = getResources().getDrawable(resId);
        if (drawable instanceof BitmapDrawable)
        {
            Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
            Bitmap reflectedBitmap = BitmapUtil.createReflectedBitmap(bitmap);
            
            return reflectedBitmap;
        }
        
        return null;
    }
}
