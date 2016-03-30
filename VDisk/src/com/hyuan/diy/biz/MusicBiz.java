package com.hyuan.diy.biz;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Audio.Media;

import com.hyuan.diy.entity.Musics;
import com.hyuan.diy.entity.Sentence;
import com.hyuan.diy.utils.Constant;

public class MusicBiz {


	public static void loadmusics(Context context) {
				ContentResolver cr=context.getContentResolver();
				Uri uri = Media.EXTERNAL_CONTENT_URI;
				Cursor c = cr.query(uri, null, null, null, null);
				for(c.moveToFirst();!c.isAfterLast();c.moveToNext()){
					Musics m = new Musics();
					if (c.getInt(c.getColumnIndex(Media.SIZE)/1024/1024)>0) {
						m.setId(c.getColumnIndex(Media._ID));
						m.setData(c.getString(c.getColumnIndex(Media.DATA)));
						m.setName(c.getString(c.getColumnIndex(Media.DISPLAY_NAME)));
						m.setSinger(c.getString(c.getColumnIndex(Media.ARTIST)));
						Constant.musicsUrl.add(c.getString(c.getColumnIndex(Media.DATA)));
						if (!Constant.musics.contains(m)) {
							Constant.musics.add(m);
						}
					}
				}
				c.close();
				
				for (int i = 0; i < 5; i++) {
					Sentence sentence = new Sentence(i+1,
							Constant.musics.get(i).getName());
					Constant.lst.add(sentence);
				}
	}

}
