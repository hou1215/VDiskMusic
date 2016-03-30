package com.hyuan.diy.view;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.hyuan.diy.R;
import com.hyuan.diy.service.PlayMusicService;
import com.hyuan.diy.utils.Constant;
import com.hyuan.diy.utils.LoadImages;
import com.hyuan.diy.utils.TimeConvert;
import com.hyuan.diy.utils.Timer;

/**
 * Created by Administrator on 2015/12/1.
 * 定时器页面
 */
/**
 * Created by Administrator on 2015/12/1.
 * 定时器页面
 */
public class TimerActivity extends Activity implements View.OnClickListener
{
    private TextView tv_time;
    private RelativeLayout time01,time02,time03,time04,time05,time06;
    private ImageView check01,check02,check03,check04,check05,check06;
    private Bitmap bitmap;
    private boolean start = false;
    private int time ;
    private static int STATE ;
    private TimePickerDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        setView();
        setListener();

    }

    private void setListener()
    {
        time01.setOnClickListener(this);
        time02.setOnClickListener(this);
        time03.setOnClickListener(this);
        time04.setOnClickListener(this);
        time05.setOnClickListener(this);
        time06.setOnClickListener(this);
    }

    private void setView()
    {
        tv_time = (TextView) findViewById(R.id.tv_time);
        time01 = (RelativeLayout) findViewById(R.id.time01);
        time02 = (RelativeLayout) findViewById(R.id.time02);
        time03 = (RelativeLayout) findViewById(R.id.time03);
        time04 = (RelativeLayout) findViewById(R.id.time04);
        time05 = (RelativeLayout) findViewById(R.id.time05);
        time06 = (RelativeLayout) findViewById(R.id.time06);
        check01 = (ImageView) findViewById(R.id.check01);
        check02 = (ImageView) findViewById(R.id.check02);
        check03 = (ImageView) findViewById(R.id.check03);
        check04 = (ImageView) findViewById(R.id.check04);
        check05 = (ImageView) findViewById(R.id.check05);
        check06 = (ImageView) findViewById(R.id.check06);
       

    }

    @Override
    protected void onResume()
    {
        loadImage();
        changeState();

        if (Timer.getLastTime() > 0)
        {
        	start = true;
            runTime();
        }
        super.onResume();
    }

    private void changeState()
    {
        int state = Timer.getSTATE();
        if (state == 2)
        {
            check02.setImageBitmap(bitmap);
        }else if (state == 3)
        {
            check03.setImageBitmap(bitmap);
        }else if (state == 4)
        {
            check04.setImageBitmap(bitmap);
        }else if (state == 5)
        {
            check05.setImageBitmap(bitmap);
        }else if (state == 6)
        {
            check06.setImageBitmap(bitmap);
        }else
        {
            check01.setImageBitmap(bitmap);
            tv_time.setText("");
        }
    }

    /**
     * 回退按钮结束当前页面
     */
    public void onBackPressed()
    {
        start = false;
        this.finish();
    }

    @Override
    public void onClick(View v)
    {
        clearCheck();
        switch (v.getId())
        {
            case R.id.time01:
                check01.setImageBitmap(bitmap);
                start = false;
                tv_time.setText("");
                STATE = 1;
                break;
            case R.id.time02:
                check02.setImageBitmap(bitmap);
                time = 10 * 60 ;
                start = true;
                STATE = 2;
                break;
            case R.id.time03:
                check03.setImageBitmap(bitmap);
                time = 30 * 60 ;
                STATE = 3;
                start = true;
                break;
            case R.id.time04:
                check04.setImageBitmap(bitmap);
                time = 60 * 60 ;
                start = true;
                STATE = 4;
                break;
            case R.id.time05:
                check05.setImageBitmap(bitmap);
                time = 60 * 60 * 2 ;
                STATE = 5;
                start = true;

                break;
            case R.id.time06:
                dialog = new TimePickerDialog(TimerActivity.this, new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {

                        int total = (hourOfDay * 60 * 60) + (60 * minute);
                        if (total > 0)
                        {
                            time = total;
                            start = true;
                            STATE = 6;
                            check06.setImageBitmap(bitmap);
                        }else
                        {
                            time = 0;
                            start = false;
                            STATE = 1;
                            check01.setImageBitmap(bitmap);
                            tv_time.setText("");
                        }

                        Intent intent = new Intent(TimerActivity.this,PlayMusicService.class);
                        intent.setAction(Constant.TIMERSTART);
                        intent.putExtra("time",time);
                        intent.putExtra("boolean",start);
                        intent.putExtra("state", STATE);
                        startService(intent);
                        runTime();

                    }
                },0,0,true);
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        changeState();
                    }
                });
                dialog.show();

                break;
        }

        Intent intent = new Intent(TimerActivity.this,PlayMusicService.class);
        intent.setAction(Constant.TIMERSTART);
        intent.putExtra("time",time);
        intent.putExtra("boolean",start);
        intent.putExtra("state",STATE);
        startService(intent);
        runTime();


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
                	Constant.LASTTIME = true;
                    while (start)
                    {
                        try
                        {
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {

                                    tv_time.setText("距离播放停止还有：");
                                    tv_time.append(TimeConvert.secToTime(time));
                                    if (time == 1 )
                                    {
                                        start = false;
                                        tv_time.setText("");
                                        Constant.LASTTIME = false;
                                        clearCheck();
                                        check01.setImageBitmap(bitmap);
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

    private void loadImage()
    {
        bitmap = LoadImages.readBitMap(this, R.drawable.check);
    }

    private void clearCheck()
    {
        check01.setImageBitmap(null);
        check02.setImageBitmap(null);
        check03.setImageBitmap(null);
        check04.setImageBitmap(null);
        check05.setImageBitmap(null);
        check06.setImageBitmap(null);
    }

}
