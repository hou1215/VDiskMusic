package com.hyuan.diy.view;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyuan.diy.R;
import com.hyuan.diy.utils.CheckAsyncTask;

public class VersionInformationActivity extends Activity {

	private TextView    		vercation;
	private RelativeLayout 		check,welcome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_version_information);
		initView();
		setListener();
	}

	private void setListener() {
		vercation.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//获得系统管理服务
				PackageManager pm = getPackageManager();
				//获得包相关信息对象
				try {
					PackageInfo info=pm.getPackageInfo(getPackageName(), 0);
					vercation.setText("版本信息    "+info.versionName);
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				
			}
		});
		check.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new CheckAsyncTask(VersionInformationActivity.this).execute();
				
			}
		});
		welcome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(VersionInformationActivity.this,WelActivity.class));
				
			}
		});
	}

	private void initView() {
		
		vercation = (TextView) findViewById(R.id.vercation);
			check = (RelativeLayout) findViewById(R.id.check);
		  welcome = (RelativeLayout) findViewById(R.id.welcome);
	}

}
