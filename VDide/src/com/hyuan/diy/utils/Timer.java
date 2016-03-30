package com.hyuan.diy.utils;

import com.hyuan.diy.myapplication.MyApplicaotion;

public class Timer
{

    private static boolean run = true;
    private static int STATE ;

    public static int getSTATE()
    {
        return STATE;
    }


    public static void setLastTime(int lastTime)
    {
        Timer.lastTime = lastTime;
    }
    static TimerThread thread = new TimerThread();
    public static void run(int time, boolean start, int state)
    {
        STATE = state;
        setLastTime(time);
        run = start;
        try
        {
            if (STATE == 1)
            {
                lastTime = 1;
                return;
            }
            if (time <= 0)
            {
                return ;
            }
            if (!thread.isAlive())
            {
                thread = new TimerThread();
            }
            thread.start();
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }
    private static int lastTime;
    public static int getLastTime()
    {
        return lastTime;
    }

    public static class TimerThread extends Thread
    {

        @Override
        public void run()
        {
            while (run)
            {
                try
                {
                    lastTime --;
                    if (lastTime == 0)
                    {
                        MyApplicaotion.getPlayService().stop();
                        STATE = 1 ;
                        run = false;
                    }
                    sleep(1000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
    }
}
