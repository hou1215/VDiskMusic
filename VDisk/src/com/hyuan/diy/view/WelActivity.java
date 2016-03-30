package com.hyuan.diy.view;

import com.hyuan.diy.R;
import com.hyuan.diy.R.id;
import com.hyuan.diy.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

public class WelActivity extends Activity {

	private ImageView  iv_cd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		iv_cd = (ImageView) findViewById(R.id.iv_cd);
		RotateAnimation an = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);  
        an.setInterpolator(new LinearInterpolator());//不停顿  
        an.setRepeatCount(1000);//重复次数  
        an.setFillAfter(true);//停在最后  
        an.setDuration(4000); 
        iv_cd.startAnimation(an);
		
	}

}
