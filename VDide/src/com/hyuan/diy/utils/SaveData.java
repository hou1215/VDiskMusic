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
         * ������ݿ������Ϊ�գ���ִ�в���
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
         * ����ղ��б��������ݿ��е��б�ִ�и��²���
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
