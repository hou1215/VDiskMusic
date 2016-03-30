package com.hyuan.diy.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyuan.diy.R;
import com.hyuan.diy.adapter.CollectAdapter;
import com.hyuan.diy.adapter.GalleryAdapter;
import com.hyuan.diy.adapter.PlayingAdapter;
import com.hyuan.diy.myapplication.MyApplicaotion;
import com.hyuan.diy.service.PlayMusicService;
import com.hyuan.diy.utils.CircleImageView;
import com.hyuan.diy.utils.Constant;
import com.hyuan.diy.utils.DeleteDialog;
import com.hyuan.diy.utils.GalleryFlow;
import com.hyuan.diy.utils.LoadImages;
import com.hyuan.diy.utils.RoundProgressBar;
import com.hyuan.diy.utils.TextFormatter;
import com.hyuan.diy.utils.TimeConvert;
import com.hyuan.diy.utils.Timer;
import com.hyuan.diy.utils.VerticalScrollTextView;
@SuppressWarnings("deprecation")
public class MainActivity extends FragmentActivity {


	private ListView                  list_lv,collect_list;
	private RelativeLayout            control ,musiclist,rltimer,collectlist,vdisklist;
	private Button                    bt_gone;

	public static ImageButton         ib_play,ib_next;
	public static CircleImageView     circle;
	public static RoundProgressBar    progressBar;
	public static TextView            tv_name,tv_singer,tv_time,mtitle,timer;
	private GalleryFlow               mGallery;

	Handler                           handler = new Handler();
	private GalleryAdapter            galleryadapter;
	public static PlayingAdapter      playingadapter;
	private VerticalScrollTextView    mSampleView;
	private int                       time;
	private long 					  mExitTime;
	private Fragment                  mFragment ;
	public static CollectAdapter 	  collectadapter;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		initView();
		
		startService(new Intent(MainActivity.this,PlayMusicService.class));
		startActivity(new Intent(MainActivity.this,VDiskActivity.class));
		//给View传递数据
		mSampleView.setList(Constant.lst);
		//更新View
		mSampleView.updateUI();	
		
        mGallery.setGravity(Gravity.CENTER_VERTICAL);
        galleryadapter = new GalleryAdapter(this);
        mGallery.setAdapter(galleryadapter);
        mGallery.setSelection(1);
        
        playingadapter = new PlayingAdapter(this, R.layout.music_item,Constant.musics);
       
        collectadapter = new CollectAdapter(this, R.layout.music_item,Constant.collectlist);
        
        list_lv.setAdapter(playingadapter);
        collect_list.setAdapter(collectadapter);
        
        
        
		
		setListener();
		
		
		
	}

	@Override
	protected void onResume() {
		if (Constant.session != null && Constant.session.isLinked()) {
			mGallery.setSelection(3);
			if (musiclist.getVisibility() == View.VISIBLE ) {
				listGone(musiclist);
			}
			if (vdisklist.getVisibility() == View.GONE ) {
				listVisible(vdisklist);
			}
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            if (mFragment.isAdded() == false)
            {
                //没添加过就添加fragment
                transaction.add(R.id.vdisklist, mFragment);
            }
            transaction.show(mFragment);
            transaction.commit();
            
		}
		if (Constant.LASTTIME) {
			rltimer.setVisibility(View.VISIBLE);
			runTime();
		}
		super.onResume();
	}
	private void runTime()
    {
        try
        {
            new Thread()
            {
                @Override
                public void run()
                {
                    while (Constant.LASTTIME)
                    {
                        try
                        {
                            runOnUiThread(new Runnable()
                            {

								@Override
                                public void run()
                                {

									timer.setText("");
									timer.append(TimeConvert.secToTime(time));
                                    if (time == 1 )
                                    {
                                    	Constant.LASTTIME = false;
                                    	rltimer.setVisibility(View.GONE);
                                    	timer.setText("");
                                    }
                                }

                            });
                            sleep(1000);
                            time = Timer.getLastTime();

                        } catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

	private void setListener() {
		try 
		{
			collect_list.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (Constant.playingUrllList != Constant.collectUrl) {
						Constant.playingUrllList = Constant.collectUrl;
					}
					if (MyApplicaotion.getPlayService() != null) {
						if (MyApplicaotion.getPlayService() != null) {
							if (Constant.collectUrl.get(position) == Constant.playingUrllList.get(Constant.playingIndex) &&
									Constant.Playing) 
							{
								MyApplicaotion.getPlayService().pause();
							}else {
								MyApplicaotion.getPlayService().start(position);
							}
							
						}
					}
					
				}
			});

			rltimer.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this,TimerActivity.class);
					startActivity(intent);
				}
			});
			
			bt_gone.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					
					if (control.getVisibility() == View.VISIBLE) {
						setGone();
					}
					
				}
			});
			
			
			
			
			
			list_lv.setOnItemClickListener(new OnItemClickListener() {

				

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (Constant.playingUrllList != Constant.musicsUrl) {
						Constant.playingUrllList = Constant.musicsUrl;
					}
					if (MyApplicaotion.getPlayService() != null) {
						if (Constant.musicsUrl.get(position) == Constant.playingUrllList.get(Constant.playingIndex) &&
								Constant.Playing) {
							MyApplicaotion.getPlayService().pause();
						}else {
							MyApplicaotion.getPlayService().start(position);
						}
						
					}
				
					/*if (control.getVisibility() == View.GONE) {
						setVisible();
					}*/
					
				}
			});
			
			list_lv.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						int position, long id) {
					DeleteDialog.delete(MainActivity.this, position);
					return false;
				}
			});
			
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
			circle.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MainActivity.this,PlayingActivity.class);
					startActivity(intent);
				}
			});
			mGallery.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view,
						int position, long id) {
					switch (position%6) {
					case 0:
						mtitle.setText("主    页");
						break;
					case 1:
						mtitle.setText("本地列表");
						break;
					case 2:
						mtitle.setText("收藏列表");
						break;
					case 3:
						mtitle.setText("微     盘");
						break;
					case 4:
						mtitle.setText("播放界面");
						break;
					case 5:
						mtitle.setText("设     置");
						break;
					}
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					
				}
			});
	        
	        mGallery.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					
					switch (position%6) {
					case 0:
						if (control.getVisibility() == View.GONE) {
							setVisible();
						}
						if (musiclist.getVisibility() == View.VISIBLE ) {
							listGone(musiclist);
						}
						//Hide the collection list
						if (collectlist.getVisibility() == View.VISIBLE ) {
							listGone(collectlist);
						}
						if (vdisklist.getVisibility() == View.VISIBLE ) {
							listGone(vdisklist);
						}
						break;
					case 1:
						if (control.getVisibility() == View.VISIBLE) {
							setGone();
						}
						if (musiclist.getVisibility() == View.GONE ) {
							listVisible(musiclist);
						}
						//Hide the collection list
						if (collectlist.getVisibility() == View.VISIBLE ) {
							listGone(collectlist);
						}
						if (vdisklist.getVisibility() == View.VISIBLE ) {
							listGone(vdisklist);
						}
						break;
					case 2:
						if (control.getVisibility() == View.VISIBLE) {
							setGone();
						}
						if (musiclist.getVisibility() == View.VISIBLE ) {
							listGone(musiclist);
						}
						//Display the list collection
						if (collectlist.getVisibility() == View.GONE ) {
							listVisible(collectlist);
						}
						if (vdisklist.getVisibility() == View.VISIBLE ) {
							listGone(vdisklist);
						}
						break;
					case 3:
						if (musiclist.getVisibility() == View.VISIBLE ) {
							listGone(musiclist);
						}
						//Hide the collection list
						if (collectlist.getVisibility() == View.VISIBLE ) {
							listGone(collectlist);
						}
						if (Constant.session != null && Constant.session.isLinked()) 
						{
							if (vdisklist.getVisibility() == View.GONE ) {
								listVisible(vdisklist);
							}
						}
						else {
							startActivity(new Intent(MainActivity.this,VDiskActivity.class));
						}
						
						break;
					case 4:
						if (musiclist.getVisibility() == View.VISIBLE ) {
							listGone(musiclist);
						}
						//Hide the collection list
						if (collectlist.getVisibility() == View.VISIBLE ) {
							listGone(collectlist);
						}
						if (vdisklist.getVisibility() == View.VISIBLE ) {
							listGone(vdisklist);
						}
						Intent intent = new Intent(MainActivity.this,PlayingActivity.class);
						startActivity(intent);
						break;
					case 5:
						if (musiclist.getVisibility() == View.VISIBLE ) {
							listGone(musiclist);
						}
						//Hide the collection list
						if (collectlist.getVisibility() == View.VISIBLE ) {
							listGone(collectlist);
						}
						if (vdisklist.getVisibility() == View.VISIBLE ) {
							listGone(vdisklist);
						}
						Intent intents = new Intent(MainActivity.this,SettingActivity.class);
						startActivity(intents);
						break;
					}

					galleryadapter.notifyDataSetChanged();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}


	private void initView() {
		
		
		mGallery    =(GalleryFlow) findViewById(R.id.gallery);
        
        mSampleView =(VerticalScrollTextView) findViewById(R.id.sampleView1);
		
		bt_gone     = (Button) findViewById(R.id.bt_gone);
		control     = (RelativeLayout) findViewById(R.id.control);
		musiclist   = (RelativeLayout) findViewById(R.id.musiclist);
		rltimer     = (RelativeLayout) findViewById(R.id.rltimer);
		collectlist = (RelativeLayout) findViewById(R.id.collectlist);
		vdisklist   = (RelativeLayout) findViewById(R.id.vdisklist);
		
		collect_list= (ListView) findViewById(R.id.collect_list);
		list_lv     = (ListView) findViewById(R.id.list_lv);
		timer       = (TextView) findViewById(R.id.timer);
		mtitle      = (TextView) findViewById(R.id.mtitle);
		tv_name     = (TextView) findViewById(R.id.tv_name);
		tv_singer   = (TextView) findViewById(R.id.tv_singer);
		tv_time     = (TextView) findViewById(R.id.tv_time);
		
		ib_play     = (ImageButton) findViewById(R.id.ib_play);
		ib_next     = (ImageButton) findViewById(R.id.ib_next);
		
		circle      = (CircleImageView) findViewById(R.id.iv_albums);
		mFragment   = new VDiskFragment();
		
		mGallery.setBackgroundColor(Color.GRAY);
        mGallery.setSpacing(60);
        mGallery.setFadingEdgeLength(2);
		circle.setImageBitmap(LoadImages.readBitMap(this, R.drawable.cd));
		progressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
		
		if (control.getVisibility() == View.VISIBLE) {
			setGone();
		}
		if (musiclist.getVisibility() == View.GONE ) {
			listVisible(musiclist);
		}
		
	}
	
	private void listGone(View view) {
		view.setVisibility(View.GONE);
		Animation a1 = new TranslateAnimation(0,  view.getWidth() ,0 , 0);
		a1.setDuration(800);
		a1.setFillAfter(false);
		view.startAnimation(a1);
		
	}

	private void listVisible(View view) {
		view.setVisibility(View.VISIBLE);
		Animation a1 = new TranslateAnimation( view.getWidth(),0 ,0 ,0);
		a1.setDuration(800);
		a1.setFillAfter(true);
		view.startAnimation(a1);
		
	}
	
	private void setVisible(){
		control.setVisibility(View.VISIBLE);
		Animation a=new TranslateAnimation(control.getWidth()+70, 0 ,0 , 0);
		a.setDuration(800);
		a.setFillAfter(true);
		control.startAnimation(a);
	}
	private void setGone() {
		control.setVisibility(View.GONE);
		Animation a=new TranslateAnimation(70, control.getWidth()+70 ,0 , 0);
		a.setDuration(600);
		a.setFillAfter(false);
		control.startAnimation(a);
		
	}
	
	public class ViewBroadcastReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			
			try {
				String action = intent.getAction();
				switch (action) { //设置歌曲信息
				case Constant.UPDATEINFO:
					
					if (Constant.Playing) {
				        	
						tv_name.setText(Constant.PlayingMusic.getName());
			        	tv_singer.setText(Constant.PlayingMusic.getSinger());
						
					}
					if (Constant.Playing) {
						ib_play.setBackgroundResource(R.drawable.stop);
						circle.roatateStart();
					}
					MyApplicaotion.getPlayService().updataList();
					break;
					
				case Constant.CDPAUSE:
					circle.roatatePause();
					ib_play.setBackgroundResource(R.drawable.play);
					break;
					
				case Constant.NEXT:
					circle.resetRoatate();
					circle.roatateStart();
					break;
				case Constant.STOP:
					circle.resetRoatate();
					ib_play.setBackgroundResource(R.drawable.play);
					progressBar.setProgress(0);
					tv_time.setText("");
					tv_singer.setText("");
					tv_name.setText("");
					break;	
				case Constant.UPDATETIME:
					int duration = intent.getIntExtra("duration", 0);
					int curentPos = intent.getIntExtra("curentPos", 0);
					progressBar.setMax(duration);
					progressBar.setProgress(curentPos);
					tv_time.setText(TextFormatter.getMusicTime(curentPos)+" - "+
										TextFormatter.getMusicTime(duration));
					break;
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			
			
		}
		
	}
	
	@Override
	protected void onDestroy() {
		MyApplicaotion.getPlayService().stop();
		super.onDestroy();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if ((System.currentTimeMillis() - mExitTime) > 2000) 
			{// 如果两次按键时间间隔大于2000毫秒，则不退出
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();// 更新mExitTime
			} else 
			{
				System.exit(0);// 否则退出程序
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
