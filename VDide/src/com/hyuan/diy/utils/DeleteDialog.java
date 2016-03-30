package com.hyuan.diy.utils;

import java.io.File;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.MediaStore;
import android.widget.Toast;

import com.hyuan.diy.entity.Musics;
import com.hyuan.diy.myapplication.MyApplicaotion;
import com.hyuan.diy.view.MainActivity;

/**
 * Created by Administrator on 2016/1/20.
 */
public class DeleteDialog
{
    /**
     * 删除点击的歌曲，弹出对话框确认操作
     * @param mContext
     * @param position
     */
    public static void delete(final Context mContext, final int position)
    {
        try
        {
        	
            final Musics music = Constant.musics.get(position);
            
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("删除歌曲");
            builder.setMessage("确定删除"+"：\n      "+music.getName());
            builder.setPositiveButton("删除", new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    File file = new File(music.getData());

                    if(file.exists())
                    {

                      //如果删除的歌曲正在播放                        
                        if (music.getData() == Constant.PlayingMusic.getData())
                        {
                        	 //如果删除的对象正在播放，确认删除后自动切换下一首
                        	MyApplicaotion.getPlayService().next();
                            Constant.musics.remove(position);
                            Constant.playingIndex = Constant.playingIndex - 1;

                        }
                      //如果删除歌曲的位置小于正在播放歌曲的位置
                        else if (position < Constant.playingIndex)
                        {
                        	Constant.playingIndex = Constant.playingIndex - 1;
                        }
                        
                        /**
                         * 如果收藏列表包含删除的歌曲、则把它移出收藏列表
                         */
                        if (Constant.musicsUrl.contains(music.getData()))
                        {
                            Constant.musicsUrl.remove(music.getData());
                            Constant.collectlist.remove(music);
                            
                            MainActivity.playingadapter.notifyDataSetChanged();
                            MainActivity.collectadapter.notifyDataSetChanged();
                            /**
                             * 保存数据
                             */
                            SaveData.updataDatabase();
                        }



                        try
                        {
                            file.delete();
                          //删除相应的media数据库中的数据
                            mContext.getContentResolver().delete(
                                    MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                                    MediaStore.Audio.Media.DATA + " = '" + music.getData() + "'", null);
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }



                        Toast.makeText(mContext, "删除成功", Toast.LENGTH_LONG).show();
                    }else
                    {
                        Toast.makeText(mContext, "歌曲不存在", Toast.LENGTH_LONG).show();
                    }
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                }
            });
            builder.create();
            builder.show();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
