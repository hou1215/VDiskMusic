package com.hyuan.diy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.InputStream;

/**
 * 加载图片，优化内存
 * Created by Administrator on 2015/12/23.
 */
public class LoadImages
{
     public static Bitmap readBitMap(Context context, int resId)
     {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inPurgeable = true;
            options.inInputShareable = true;
            //鑾峰彇璧勬簮鍥剧墖
            InputStream is = context.getResources().openRawResource(resId);
            return BitmapFactory.decodeStream(is,null,options);
     }
}
