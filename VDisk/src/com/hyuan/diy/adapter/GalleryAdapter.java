package com.hyuan.diy.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.hyuan.diy.utils.Constant;

public class GalleryAdapter extends BaseAdapter{

	private Context mContext;
		
	public GalleryAdapter(Context mContext){  
		this.mContext=mContext;  
		
	}  
	@Override
    public int getCount()
    {
        return Integer.MAX_VALUE;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
    	if (null == convertView)
        {
            convertView = new MyImageView(mContext);
            convertView.setLayoutParams(new Gallery.LayoutParams(110, 184));
        }
        
        ImageView imageView = (ImageView)convertView;
    	try 
    	{
            imageView.setImageDrawable(Constant.mBitmaps.get(position%Constant.mBitmaps.size()));
           
            imageView.setScaleType(ScaleType.FIT_XY); 
            
		} catch (Exception e) {
			e.printStackTrace();
		}
    	
        

        return imageView;
    } 
    
    
    private class MyImageView extends ImageView
    {
        public MyImageView(Context context)
        {
            this(context, null);
        }
        
        public MyImageView(Context context, AttributeSet attrs)
        {
            super(context, attrs, 0);
        }
        
        public MyImageView(Context context, AttributeSet attrs, int defStyle)
        {
            super(context, attrs, defStyle);
        }
        
        protected void onDraw(Canvas canvas)
        {
            super.onDraw(canvas);
        }
    }


} 
