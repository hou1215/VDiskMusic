package com.hyuan.diy.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import android.database.Cursor;
import android.util.Log;

import com.hyuan.diy.entity.Musics;


/**
 * Created by Administrator on 2016/1/22.
 */
public class LoadCollectionDb
{

    /**
     * ͨ�� cursor �����ȡ���ݣ���ת���� json
     * �� json ת���� JSONObject����װ�� collectDataBaseList �С�
     * @param cursor
     */
    public static void tracks(Cursor cursor)
    {
        String json = null;
        for(cursor.moveToFirst();!cursor.isAfterLast(); cursor.moveToNext())
        {
            json = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(1)));
        }
        cursor.close();

        if (json != null)
        {
            try
            {
                JSONObject  dataJson = new JSONObject(json);
                JSONArray tracks = dataJson.getJSONArray("musicss");
                
                for (int i = 0; i < tracks.length(); i++)
                {
                	Musics track = new Musics();
                    JSONObject trackJson = tracks.getJSONObject(i);

                    track.setData(trackJson.getString("data"));
                    track.setSinger(trackJson.getString("singer"));
                    track.setName(trackJson.getString("name"));
                    track.setId(trackJson.getInt("id"));
                    Constant.collectDb.add(track);
                }

            }catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
