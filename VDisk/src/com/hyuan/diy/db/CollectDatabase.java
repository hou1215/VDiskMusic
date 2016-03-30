package com.hyuan.diy.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/1/21.
 */
public class CollectDatabase extends SQLiteOpenHelper
{

    public CollectDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, name, factory, version);
    }

    /**
     * 创建数据库
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String sql = "create table if not exists musicsColleck(m_name varchar(10), m_data varchar);";
        db.execSQL(sql);
    }

    /**
     * inset the data
     * @param table
     * @param values
     */
    public void inset(String table,  ContentValues values)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.insert(table, null, values);
        db.close();
    }

    /**
     * update the database
     * @param table
     * @param values
     * @param whereClause
     * @param whereArgs
     */
    public void update(String table, ContentValues values, String whereClause, String[] whereArgs)
    {
        SQLiteDatabase db = getWritableDatabase();
        db.update(table, values, whereClause, whereArgs);
        db.close();
    }

    /**
     * query the database
     * @param table
     * @param columns
     * @param selection
     * @param orderBy
     * @return
     */
    public Cursor query(String table, String[] columns, String selection, String orderBy)
    {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(table, columns, selection, null, null, null, orderBy);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
