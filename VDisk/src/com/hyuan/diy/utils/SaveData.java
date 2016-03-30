package com.hyuan.diy.utils;

import android.content.ContentValues;

import com.hyuan.diy.myapplication.MyApplicaotion;

/**
 * Created by Administrator on 2016/1/26.
 */
public class SaveData
{
    public static void updataDatabase()
    {

        /**
         * 如果数据库的数据为空，则执行插入
         */
        if (Constant.collectlist.size() == 1)
        {
            String json = ListToJson.listToJson(Constant.collectlist);
            ContentValues values = new ContentValues();
            values.put("m_name","musicsColleck");
            values.put("m_data",json);
            MyApplicaotion.collectDB.inset("musicsColleck", values);
        }
        /**
         * 如果收藏列表不等于数据库中的列表，执行更新操作
         */
        else
        {
            String json = ListToJson.listToJson(Constant.collectlist);
            ContentValues values = new ContentValues();
            values.put("m_name","musicsColleck");
            values.put("m_data",json);
            MyApplicaotion.collectDB.update("musicsColleck", values, "m_name=?", new String[]{"musicsColleck"});
        }

    }
}
