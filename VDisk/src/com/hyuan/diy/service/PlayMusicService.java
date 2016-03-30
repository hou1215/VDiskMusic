package com.hyuan.diy.service;


import java.util.Random;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Binder;
import android.os.IBinder;

import com.hyuan.diy.biz.PlayingBiz;
import com.hyuan.diy.entity.Musics;
import com.hyuan.diy.myapplication.MyApplicaotion;
import com.hyuan.diy.utils.Constant;
import com.hyuan.diy.utils.Timer;
import com.hyuan.diy.view.MainActivity;
import com.hyuan.diy.view.VDiskFragment;
import com.hyuan.diy.view.MainActivity.ViewBroadcastReceiver;

public class PlayMusicService extends Service implements PlayingBiz, OnCompletionListener{
	private MediaPlayer    			mediaPlayer ;
	private IBinder 				mybinder = new MyBinder();
	private ViewBroadcastReceiver   receiver;
	private int 					currentPausePos ;
	private Intent 					intent = new Intent();

	public PlayMusicService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mybinder;
	}
	
	@Override
	public void onCreate() {
		mediaPlayer = new MediaPlayer();
		if (Constant.playingUrllList.size() <= 0) {
			Constant.playingUrllList = Constant.musicsUrl;
		}
		mediaPlayer.setOnCompletionListener(this);
		receiver = new MainActivity().new ViewBroadcastReceiver();
		IntentFilter filter=new IntentFilter();
		filter.addAction(Constant.UPDATEINFO);
		filter.addAction(Constant.CDSTART);
		filter.addAction(Constant.CDPAUSE);
		filter.addAction(Constant.NEXT);
		filter.addAction(Constant.PREV);
		filter.addAction(Constant.STOP);
		filter.addAction(Constant.UPDATETIME);
		registerReceiver(receiver, filter);
		new MusicDurtion().start();
		super.onCreate();
	}
	
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (MyApplicaotion.getPlayService() == null) {
			return super.onStartCommand(intent, flags, startId);
		}
		String action = intent.getAction();
		if (action == Constant.TIMERSTART ) {
			int time = intent.getIntExtra("time", 0);
            boolean start = intent.getBooleanExtra("boolean", false);
            int state = intent.getIntExtra("state",0);
            timer(time, start,state);
		}
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private void timer(int time,boolean start,int state)
    {
        Timer.run(time, start, state);
    }
	
	public class MyBinder extends Binder{
        
        public PlayMusicService getService(){
            return PlayMusicService.this;
        }
    }
	
	private void changeInfo(){
		intent.setAction(Constant.UPDATEINFO);
		sendBroadcast(intent);

	}

	@Override
	public void onDestroy() {
		unregisterReceiver(receiver);
		Constant.Playing = false;
		playing = false;
		super.onDestroy();
	}
	
	private Boolean playing = true;
	public class MusicDurtion extends Thread{
		@Override
		public void run() {
			try{
				while (playing) {
				if (mediaPlayer.isPlaying()) {
					if (Constant.UPDATE) {
						Intent intent = new Intent(Constant.UPDATETIME);
						intent.putExtra("duration", mediaPlayer.getDuration());
						intent.putExtra("curentPos", mediaPlayer.getCurrentPosition());
						sendBroadcast(intent);
					}
				}
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			}catch(Exception e){
				
			}
		}
	}
	
	
	@Override
	public void start(int position) {
		try {
			Constant.playingIndex = position;
			mediaPlayer.reset();
			mediaPlayer.setDataSource(Constant.playingUrllList.get(position));
			mediaPlayer.prepare();
			mediaPlayer.start();
			
			if (Constant.playingUrllList == Constant.musicsUrl)
	        {
				Constant.PlayingMusic = Constant.musics.get(position);
	        }
	        else if (Constant.playingUrllList == Constant.collectUrl)
	        {
	        	Constant.PlayingMusic = Constant.collectlist.get(position);
	        }
			 
			Constant.Playing = true;
			changeInfo();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}


	@Override
	public void pause() {
		currentPausePos = mediaPlayer.getCurrentPosition();
		mediaPlayer.pause();
		Constant.Playing = false;
		
		
		intent.setAction(Constant.CDPAUSE);
		sendBroadcast(intent);
		
	}

	@Override
	public void next() {
		try 
		{
			if (Constant.playingIndex == Constant.playingUrllList.size()-1) {
				Constant.playingIndex = -1;
			}
			Constant.playingIndex ++;
			
			if (Constant.playingUrllList == Constant.VDiskList) 
			{
				String filename = Constant.VDiskList.get(Constant.playingIndex);
				if (filename.contains(".")) {
					String type = filename.substring(filename.lastIndexOf('.'));
					if (type.equals(".mp3")||type.equals(".wav")||type.equals(".flac")||type.equals(".")) {
						VDiskFragment.getDownloadUrl(Constant.VDiskList.get(Constant.playingIndex));
					}
					else {
						next();
					}
				}
				
				else {
					next();
				}
				
			}else 
			{
				start(Constant.playingIndex);
			}
			
			
			if (Constant.playingUrllList == Constant.musicsUrl)
	        {
				Constant.PlayingMusic = Constant.musics.get(Constant.playingIndex);
	        }
	        else if (Constant.playingUrllList == Constant.collectUrl)
	        {
	        	Constant.PlayingMusic = Constant.collectlist.get(Constant.playingIndex);
	        }else {
	        	
			}
			
			intent.setAction(Constant.NEXT);
			sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void prev() {
		try 
		{
			if (Constant.playingIndex == 0)
			{
				Constant.playingIndex = Constant.playingUrllList.size();
			}
			Constant.playingIndex --;
			
			if (Constant.playingUrllList == Constant.VDiskList) 
			{
				String filename = Constant.VDiskList.get(Constant.playingIndex);
				String type = filename.substring(filename.lastIndexOf('.'));
				if (type.equals(".mp3")||type.equals(".wav")||type.equals(".flac")||type.equals(".")) {
					VDiskFragment.getDownloadUrl(Constant.VDiskList.get(Constant.playingIndex));
				}
				else {
					prev();
				}
				
			}else 
			{
				start(Constant.playingIndex);
			}
			
			if (Constant.playingUrllList == Constant.musicsUrl)
	        {
				Constant.PlayingMusic = Constant.musics.get(Constant.playingIndex);
	        }
	        else if (Constant.playingUrllList == Constant.collectUrl)
	        {
	        	Constant.PlayingMusic = Constant.collectlist.get(Constant.playingIndex);
	        }else {
	        	
			}
			
			intent.setAction(Constant.NEXT);
			sendBroadcast(intent);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public void restart() {
		if (Constant.playingIndex == 0) {
			start(Constant.playingIndex);
		}
		else {
			mediaPlayer.seekTo(currentPausePos);
			mediaPlayer.start();
			Constant.Playing = true;
			changeInfo();
		}
		
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		
		if (Constant.player_mode%3 == Constant.LISTLOOP) {
			next();
		}
		else if (Constant.player_mode%3 == Constant.SINGLELOOP) {
			mp.isLooping();
			mp.start();
		}
		else if (Constant.player_mode%3 == Constant.RANDOM){
			Constant.playingIndex = new Random().nextInt(Constant.playingUrllList.size());
			
			if (Constant.playingUrllList == Constant.VDiskList) 
			{
				String filename = Constant.VDiskList.get(Constant.playingIndex);
				String type = filename.substring(filename.lastIndexOf('.'));
				if (type.equals(".mp3")||type.equals(".wav")||type.equals(".flac")||type.equals(".")) 
				{
					VDiskFragment.getDownloadUrl(Constant.VDiskList.get(Constant.playingIndex));
				}
				else 
				{
					next();
				}
				
			}else 
			{
				start(Constant.playingIndex);
			}
		}
		
	}

	@Override
	public void seekTo(float progress) {
		mediaPlayer.seekTo((int) progress);
		mediaPlayer.start();
		Constant.Playing = true;
		
		changeInfo();
	}

	@Override
	public void stop() {
		mediaPlayer.stop();
		mediaPlayer.reset();
		intent.setAction(Constant.STOP);
		sendBroadcast(intent);
	}

	@Override
	public void start(String url,String fileName) {
		try {
			mediaPlayer.reset();
			mediaPlayer.setDataSource(url);
			mediaPlayer.prepare();
			mediaPlayer.start();
			
			Constant.PlayingMusic = new Musics();
			Constant.PlayingMusic.setName(fileName);
			Constant.PlayingMusic.setSinger("unkown");
			
			Constant.Playing = true;
			changeInfo();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void updataList() {
		//如果正在播放的列表是本地的列表
        if (Constant.playingUrllList == Constant.musicsUrl)
        {
        	Constant.mHighlightIndex.clear();
        	Constant.mHighlightIndex.add(Constant.playingIndex);
        	Constant.mHighlightIndex.add(-1);
        	Constant.mHighlightIndex.add(-1);
        }
        else if (Constant.playingUrllList == Constant.collectUrl)
        {
        	Constant.mHighlightIndex.clear();
        	Constant.mHighlightIndex.add(-1);
        	Constant.mHighlightIndex.add(Constant.playingIndex);
        	Constant.mHighlightIndex.add(-1);
        }else {
        	Constant.mHighlightIndex.clear();
        	Constant.mHighlightIndex.add(-1);
        	Constant.mHighlightIndex.add(-1);
        	Constant.mHighlightIndex.add(Constant.playingIndex);
		}
        
        MainActivity.playingadapter.notifyDataSetChanged();
        MainActivity.collectadapter.notifyDataSetChanged();
        VDiskFragment.adapter.notifyDataSetChanged();
		
	}


}
