package com.hyuan.diy.view;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyuan.diy.R;
import com.hyuan.diy.myapplication.MyApplicaotion;
import com.hyuan.diy.utils.Constant;
import com.hyuan.diy.utils.RectBar;
import com.hyuan.diy.utils.RectBar.SeekToListener;
import com.hyuan.diy.utils.SaveData;

public class PlayingActivity extends Activity {

	private RectBar 				rectbar;
	private int						width;
	private int 					height;
	private float 					progress;
	private float 					scale;
	private ViewBroadcastReceiver 	receiver;
	private TextView 				tv_name,tv_singer;
	private ImageView 				ib_play,ib_next,ib_prev,iv_collect,player_mode;
	private int 					duration;
	private int 					curentPos;
	
	Handler handler = new Handler();
	private SharedPreferences preferences;
	private Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_playing);
		WindowManager windowManager = getWindowManager();    
        Display display = windowManager.getDefaultDisplay();    
        width = display.getWidth();    
        height = display.getHeight();
        scale = getResources().getDisplayMetrics().density; 
        // 获取只能被本应用程序读、写的SharedPreferences对象  
        preferences = getSharedPreferences("mode",MODE_PRIVATE); 
        Constant.player_mode = preferences.getInt("mode", 0);
        //获得修改器  
        editor = preferences.edit(); 
        
		initView();
		
		setListener();
	}

	private void setListener() {
		ib_play.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (Constant.Playing) {
					MyApplicaotion.getPlayService().pause();
				}
				else {
					if (Constant.playingUrllList.size() <= 0) {
						Constant.playingUrllList = Constant.musicsUrl;
					}
					MyApplicaotion.getPlayService().restart();
				}			
			}
		});
		ib_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ib_next.setEnabled(false);
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						ib_next.setEnabled(true);
					}
				}, 1000);
				if (MyApplicaotion.getPlayService() != null) {
					MyApplicaotion.getPlayService().next();
				}
			}
		});
		ib_prev.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ib_prev.setEnabled(false);
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						ib_prev.setEnabled(true);
					}
				}, 1000);
				if (MyApplicaotion.getPlayService() != null) {
					MyApplicaotion.getPlayService().prev();
				}
			}
		});
		rectbar.SeekToListener(new SeekToListener() {
			
			@Override
			public void SeekTo(float progress) {
				rectbar.setProgress(progress);
				MyApplicaotion.getPlayService().seekTo(progress*duration/width);
			}
		});
		iv_collect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (!Constant.collectUrl.contains(Constant.playingUrllList.get(Constant.playingIndex))) {
					Constant.collectUrl.add(Constant.playingUrllList.get(Constant.playingIndex));
					Constant.collectlist.add(Constant.PlayingMusic);
					
					iv_collect.setImageResource(R.drawable.collect);
					
					MainActivity.playingadapter.notifyDataSetChanged();
                	MainActivity.collectadapter.notifyDataSetChanged();
					/**
                     * 保存数据
                     */
                    SaveData.updataDatabase();
				}
				else 
				{
					AlertDialog.Builder builder = new AlertDialog.Builder(PlayingActivity.this);
                    builder.setMessage("是否要移除"+"\n"+Constant.PlayingMusic.getName());
                    builder.setTitle("移除收藏");
                    builder.setPositiveButton("移除", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        	Constant.collectlist.remove(Constant.PlayingMusic);
        					Constant.musicsUrl.remove(Constant.PlayingMusic.getData());
                        	
                    		iv_collect.setImageResource(R.drawable.uncollect);
                        	MainActivity.playingadapter.notifyDataSetChanged();
                        	MainActivity.collectadapter.notifyDataSetChanged();
                        	
                        	
                        	/**
                             * 保存数据
                             */
                            SaveData.updataDatabase();
                        	
        					}
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
				}
				
			}
		});
		
		player_mode.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Constant.player_mode++;
				if (Constant.player_mode%3 == Constant.LISTLOOP) {
					player_mode.setImageResource(R.drawable.loper);
				}
				else if (Constant.player_mode%3 == Constant.SINGLELOOP) {
					player_mode.setImageResource(R.drawable.singerloop);
				}
				else  if (Constant.player_mode%3 == Constant.RANDOM){
					player_mode.setImageResource(R.drawable.random);
				}
				editor.putInt("mode", Constant.player_mode%3);
				editor.commit();
			}
		});
		
	}

	private void initView() {
		try 
		{
			receiver = new ViewBroadcastReceiver();
			IntentFilter filter=new IntentFilter();
			filter.addAction(Constant.UPDATEINFO);
			filter.addAction(Constant.CDSTART);
			filter.addAction(Constant.CDPAUSE);
			filter.addAction(Constant.NEXT);
			filter.addAction(Constant.PREV);
			filter.addAction(Constant.STOP);
			filter.addAction(Constant.UPDATETIME);
			registerReceiver(receiver, filter);
			
			rectbar    = (RectBar) findViewById(R.id.rectbar);
			tv_name    = (TextView) findViewById(R.id.tv_name);
			tv_singer  = (TextView) findViewById(R.id.tv_singer);
			if (Constant.PlayingMusic != null) {
				tv_name.setText(Constant.PlayingMusic.getName());
				tv_singer.setText(Constant.PlayingMusic.getSinger());
			}
			
			ib_play    = (ImageView) findViewById(R.id.ib_play);
			ib_next    = (ImageView) findViewById(R.id.ib_next);
			ib_prev    = (ImageView) findViewById(R.id.ib_prev);
			iv_collect = (ImageView) findViewById(R.id.iv_collect);
			player_mode= (ImageView) findViewById(R.id.player_mode);
			if (Constant.player_mode%3 == Constant.LISTLOOP) {
				player_mode.setImageResource(R.drawable.loper);
			}
			else if (Constant.player_mode%3 == Constant.SINGLELOOP) {
				player_mode.setImageResource(R.drawable.singerloop);
			}
			else if (Constant.player_mode%3 == Constant.RANDOM){
				player_mode.setImageResource(R.drawable.random);
			}
			
			if (Constant.PlayingMusic != null && Constant.musicsUrl.contains(Constant.PlayingMusic.getData())) {
				iv_collect.setImageResource(R.drawable.collect);
			}else {
				iv_collect.setImageResource(R.drawable.uncollect);
			}
			
			if (Constant.Playing) {
				ib_play.setBackgroundResource(R.drawable.stop);
			}else {
				ib_play.setBackgroundResource(R.drawable.play);
			}
			
			rectbar.setHeight(height-(235 * scale + 0.5f));
			rectbar.setMax(width);
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}
	
	
	
	public class ViewBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			try {
				String action = intent.getAction();
				switch (action) { //设置歌曲信息
				case Constant.UPDATEINFO:
					if (Constant.playingUrllList == Constant.VDiskList) {
						iv_collect.setImageResource(0);
						iv_collect.setEnabled(false);
					}
					else {
						iv_collect.setEnabled(true);
					}
					if (Constant.PlayingMusic != null) {
						tv_singer.setText(Constant.PlayingMusic.getSinger());
						tv_name.setText(Constant.PlayingMusic.getName());
					}
					if (Constant.Playing) {
						ib_play.setBackgroundResource(R.drawable.stop);
					}
					if (Constant.musicsUrl.contains(Constant.PlayingMusic.getData())) {
						iv_collect.setImageResource(R.drawable.collect);
					}else {
						iv_collect.setImageResource(R.drawable.uncollect);
					}
					break;
					
				case Constant.CDPAUSE:
					ib_play.setBackgroundResource(R.drawable.play);
					break;
				case Constant.PREV:
					tv_singer.setText(Constant.PlayingMusic.getSinger());
					tv_name.setText(Constant.PlayingMusic.getName());
					break;
				case Constant.STOP:
					rectbar.setProgress(0);
					rectbar.setCurentPostime(0);
					rectbar.setTotalTime(0);
					ib_play.setBackgroundResource(R.drawable.play);
					break;
				case Constant.NEXT:
					tv_singer.setText(Constant.PlayingMusic.getSinger());
					tv_name.setText(Constant.PlayingMusic.getName());
					break;
					
				case Constant.UPDATETIME:
					duration = intent.getIntExtra("duration", 0);
					curentPos = intent.getIntExtra("curentPos", 0);
					progress = width*curentPos/duration;
					rectbar.setProgress(progress);
					rectbar.setCurentPostime(curentPos);
					rectbar.setTotalTime(duration);
					//rectbar.setDurationTime(TextFormatter.getMusicTime(curentPos));
					//rectbar.setTotlaTime(TextFormatter.getMusicTime(duration));
					break;
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
		}
		
	}
	@Override
	protected void onDestroy() {
		unregisterReceiver(receiver);
		super.onDestroy();
	}
}
