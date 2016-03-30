package com.hyuan.diy.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.hyuan.diy.R;
import com.hyuan.diy.utils.AccessTokenKeeper;

public class SettingActivity extends Activity {

	private RelativeLayout logout,rl_timer,rl_version_information;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		
		setView();
        setClickListener();
	}

	/**
     * 设置按钮监听，点击跳转页面
     */
    private void setClickListener()
    {
        rl_version_information.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(SettingActivity.this, VersionInformationActivity.class));
            }
        });
        rl_timer.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(SettingActivity.this, TimerActivity.class);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (VDiskFragment.session != null) {
					VDiskFragment.session.unlink();
					AccessTokenKeeper.clear(SettingActivity.this);
					Toast.makeText(SettingActivity.this, "微盘已退出", 0).show();
				}
			}
		});
        
    }

    /**
     * 初始化控件
     */
    private void setView()
    {
        rl_version_information = (RelativeLayout) findViewById(R.id.version_information);
        			  rl_timer = (RelativeLayout) findViewById(R.id.timer);
        			    logout = (RelativeLayout) findViewById(R.id.logout);
    }
	
}
