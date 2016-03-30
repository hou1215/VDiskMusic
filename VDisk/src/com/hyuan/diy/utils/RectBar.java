package com.hyuan.diy.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.hyuan.diy.R;

public class RectBar extends View{
	
	private int curentPostime,totalTime;
	
	public void setCurentPostime(int curentPostime) {
		this.curentPostime = curentPostime;
	}
	

	public void setTotalTime(int totalTime) {
		this.totalTime = totalTime;
	}

	private Paint paintRect = new Paint();
	private Paint paintLine = new Paint();
	
	
	/**
	 * 进度条
	 */
	private Paint paintRectBar = new Paint();
	private Paint paintRectBarBG = new Paint();
	private Paint paintLineBar = new Paint();
	
	/**
	 * 绘制时间
	 */
	private Paint paintCurentPosTime = new Paint();
	private Paint paintTotlaTime = new Paint();
	/**
	 * 最大进度
	 */
	private float max;
	/**
	 * 当前进度
	 */
	private float progress;
	/**
	 * 进度条高度
	 */
	private float barHeigth ;
	
	private float height;
	
	public void setHeight(float height) {
		this.height = height;
	}
	
	private float startX = 0,startY = 0;
	private float scale;
	
	public RectBar(Context context, AttributeSet attrs){
		super(context, attrs);
		scale = context.getResources().getDisplayMetrics().density;  
	}
	
	public RectBar(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		
	}

	@Override
	protected void onDraw(Canvas canvas) {
		barHeigth = height -(20 / scale + 0.5f);
		
		/**
		 * 绘制时间
		 */
		paintCurentPosTime.setStyle(Paint.Style.FILL);
		paintCurentPosTime.setTextSize( 65); 
		paintCurentPosTime.setAntiAlias(true);// 消除锯齿
		paintCurentPosTime.setColor(getResources().getColor(R.color.timeColor));
		canvas.drawText(TextFormatter.getMusicTime(curentPostime),
				progress - 180, barHeigth -20, paintCurentPosTime);
		
		paintTotlaTime.setStyle(Paint.Style.FILL);
		paintTotlaTime.setTextSize( 20);
		paintTotlaTime.setAntiAlias(true);// 消除锯齿
		paintTotlaTime.setColor(getResources().getColor(R.color.timeColor));
		canvas.drawText(TextFormatter.getMusicTime(totalTime),
				max - 70, barHeigth -20, paintTotlaTime);
		
		/**
		 * 绘制模糊背景
		 */
		paintRect.setStyle(Paint.Style.FILL);
		paintRect.setAntiAlias(true);// 消除锯齿
		paintRect.setColor(getResources().getColor(R.color.rectColor));
		canvas.drawRect(startX, startY, progress, height, paintRect);
		/**
		 * 绘制线条
		 */
		paintLine.setStyle(Paint.Style.FILL);
		paintLine.setAntiAlias(true);// 消除锯齿
		paintLine.setColor(getResources().getColor(R.color.lineColor));
		paintLine.setStrokeWidth((float) 3);
		canvas.drawLine(progress,startX , progress, height, paintLine);
		
		/**
		 * 绘制进度背景
		 */
		paintRectBarBG.setStyle(Paint.Style.FILL);
		paintRectBarBG.setAntiAlias(true);// 消除锯齿
		paintRectBarBG.setColor(getResources().getColor(R.color.rectBarbg));
		canvas.drawRect(startX, barHeigth, max, height, paintRectBarBG);
		/**
		 * 绘制进度
		 */
		paintRectBar.setStyle(Paint.Style.FILL);
		paintRectBar.setAntiAlias(true);// 消除锯齿
		paintRectBar.setColor(getResources().getColor(R.color.rectColorBar));
		canvas.drawRect(startX, barHeigth, progress, height, paintRectBar);
		/**
		 * 绘制进度条线条
		 */
		paintLineBar.setStyle(Paint.Style.FILL);
		paintLineBar.setAntiAlias(true);// 消除锯齿
		paintLineBar.setColor(getResources().getColor(R.color.lineBarbg));
		paintLineBar.setStrokeWidth((float) 4);
		canvas.drawLine(progress,barHeigth-3, progress, height, paintLineBar);
		
		
		
		

		super.onDraw(canvas);
	}
	
	public synchronized float getMax()
	{
		return max;
	}

	/**
	 * 设置进度的最大值
	 * @param max
	 */
	public synchronized void setMax(int max)
	{
		if(max <= 0)
		{
			throw new IllegalArgumentException("Max can not less than 0.");
		}
		this.max = max;
	}
	
	/**
	 * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
	 * 刷新界面调用postInvalidate()能在非UI线程刷新
	 * @param progress
	 */
	public synchronized void setProgress(float progress)
	{
		try {
			if(progress < 0)
			{
				throw new IllegalArgumentException("progress not less than 0");
			}
			if(progress > max)
			{
				progress = max;
			}
			if(progress <= max)
			{
				this.progress = progress;
				postInvalidate();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	float xDown  ,xUp ,xMove ;
	
    /**
     * 拖动条拖动事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
            	xDown = event.getX();
            	Constant.UPDATE = false;
                break;
            case MotionEvent.ACTION_MOVE:
            	xMove = event.getX();
            	if (seekToListener != null)
                {
            		progress = xMove;
            		curentPostime = (int) (progress*totalTime/max);
                }
            	
        		
                break;
            case MotionEvent.ACTION_UP:
            	xUp = event.getX();
            	seekToListener.SeekTo(progress);
            	/*if (seekToListener != null && 
            			((progress - 20)<= xDown && xDown <= progress ) ||
            			((progress + 20)>= xDown && xDown >= progress ))
                {
            		progress = progress + (xUp - xDown);
            		seekToListener.SeekTo(progress);
                }
            	if (seekToListener != null && (xUp - xDown) <= 10 && (xDown - xUp) <= 10) {
            		progress = xUp;
            		seekToListener.SeekTo(progress);
				}*/
            	Constant.UPDATE = true;
                break;
            default:
                break;
        }
        postInvalidate();
        return true;
    }
    /**
     * Key up callback.
     */
    private SeekToListener seekToListener;
    public void SeekToListener(SeekToListener seekToListener)
    {
        this.seekToListener = seekToListener;
    }
    public interface SeekToListener
    {
        void SeekTo(float progress);
    }
	
}
