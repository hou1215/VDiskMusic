package com.hyuan.diy.myapplication;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.hyuan.diy.db.CollectDatabase;
import com.hyuan.diy.service.PlayMusicService;
import com.hyuan.diy.service.PlayMusicService.MyBinder;
import com.hyuan.diy.utils.Constant;

public class MyApplicaotion extends Application{

	private static PlayMusicService playService;
	public static CollectDatabase collectDB;
	
	public static PlayMusicService getPlayService() {
		return playService;
	}


	@Override
	public void onCreate() {
		collectDB = new CollectDatabase(this,"musicsColleck.db",null,1);
		if (playService == null) {
			Intent intent = new Intent(this,PlayMusicService.class);
			bindService(intent, conn, Context.BIND_AUTO_CREATE);
		}
		
		Constant.mHighlightIndex.add(-1);
		Constant.mHighlightIndex.add(-1);
		Constant.mHighlightIndex.add(-1);
		
		super.onCreate();
	}
	
	


	ServiceConnection conn = new ServiceConnection(){


		@Override
		  public void onServiceConnected(ComponentName name, IBinder service) {
			  MyBinder binder = (PlayMusicService.MyBinder) service;
			  playService = binder.getService();
		  }

		  @Override
		  public void onServiceDisconnected(ComponentName name) {
			  
			  playService = null;
		  }

		};
		
	
}
