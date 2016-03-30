package com.hyuan.diy.utils;

import java.util.ArrayList;
import java.util.List;

import android.graphics.drawable.BitmapDrawable;

import com.hyuan.diy.entity.Musics;
import com.hyuan.diy.entity.Sentence;
import com.vdisk.android.VDiskAuthSession;

public class Constant {
	
	public static List<String> musicsUrl   	 		 = new ArrayList<>();
	public static List<String> collectUrl   	 	 = new ArrayList<>();
	public static List<String> VDiskList   	 		 = new ArrayList<>();
	public static List<String> playingUrllList   	 = new ArrayList<>();
	public static ArrayList<Musics> collectlist   	 = new ArrayList<Musics>();
	public static ArrayList<Musics> musics   		 = new ArrayList<Musics>();
	public static ArrayList<Musics> collectDb  	 	 = new ArrayList<Musics>();
	public static Boolean Playing            		 = false;
	public static Musics PlayingMusic ;
	public static final String UPDATEINFO    		 = "com.diy.changeMusicInfo";
	public static final String CDSTART       		 = "com.diy.cd.start";
	public static final String CDPAUSE        		 = "com.diy.cd.pause";
	public static final String STOP 		  		 = "com.diy.stop";
	public static final String NEXT 		  		 = "com.diy.next";
	public static final String PREV			  		 = "com.diy.prev";
	public static final String UPDATETIME 	  		 = "com.diy.updatetime";
	public static final String TIMERSTART	  		 = "com.diy.timerStart";
	public static boolean LASTTIME 			  		 = false;
	public static List<Sentence> lst		  		 = new ArrayList<Sentence>();
	public static ArrayList<BitmapDrawable> mBitmaps = new ArrayList<BitmapDrawable>();
	public static Boolean UPDATE 					 = true;
	public static int playingIndex					 = 0 ;
	public static VDiskAuthSession  session;
	/**
     * 存放歌曲高亮设置的textview
     */
    //public static List<TextView> mTextViewList 		 = new ArrayList<>();
    /**
     * 存放歌曲高亮设置的下标
     */
    public static List<Integer> mHighlightIndex 	 = new ArrayList<>();
    
    public static int player_mode ;
    public static int  RANDOM                     	 = 0;
    public static int SINGLELOOP                     = 1;
    public static int LISTLOOP                       = 2;
	
}
