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
	 * ������
	 */
	private Paint paintRectBar = new Paint();
	private Paint paintRectBarBG = new Paint();
	private Paint paintLineBar = new Paint();
	
	/**
	 * ����ʱ��
	 */
	private Paint paintCurentPosTime = new Paint();
	private Paint paintTotlaTime = new Paint();
	/**
	 * ������
	 */
	private float max;
	/**
	 * ��ǰ����
	 */
	private float progress;
	/**
	 * �������߶�
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
		 * ����ʱ��
		 */
		paintCurentPosTime.setStyle(Paint.Style.FILL);
		paintCurentPosTime.setTextSize( 65); 
		paintCurentPosTime.setAntiAlias(true);// �������
		paintCurentPosTime.setColor(getResources().getColor(R.color.timeColor));
		canvas.drawText(TextFormatter.getMusicTime(curentPostime),
				progress - 180, barHeigth -20, paintCurentPosTime);
		
		paintTotlaTime.setStyle(Paint.Style.FILL);
		paintTotlaTime.setTextSize( 20);
		paintTotlaTime.setAntiAlias(true);// �������
		paintTotlaTime.setColor(getResources().getColor(R.color.timeColor));
		canvas.drawText(TextFormatter.getMusicTime(totalTime),
				max - 70, barHeigth -20, paintTotlaTime);
		
		/**
		 * ����ģ������
		 */
		paintRect.setStyle(Paint.Style.FILL);
		paintRect.setAntiAlias(true);// �������
		paintRect.setColor(getResources().getColor(R.color.rectColor));
		canvas.drawRect(startX, startY, progress, height, paintRect);
		/**
		 * ��������
		 */
		paintLine.setStyle(Paint.Style.FILL);
		paintLine.setAntiAlias(true);// �������
		paintLine.setColor(getResources().getColor(R.color.lineColor));
		paintLine.setStrokeWidth((float) 3);
		canvas.drawLine(progress,startX , progress, height, paintLine);
		
		/**
		 * ���ƽ��ȱ���
		 */
		paintRectBarBG.setStyle(Paint.Style.FILL);
		paintRectBarBG.setAntiAlias(true);// �������
		paintRectBarBG.setColor(getResources().getColor(R.color.rectBarbg));
		canvas.drawRect(startX, barHeigth, max, height, paintRectBarBG);
		/**
		 * ���ƽ���
		 */
		paintRectBar.setStyle(Paint.Style.FILL);
		paintRectBar.setAntiAlias(true);// �������
		paintRectBar.setColor(getResources().getColor(R.color.rectColorBar));
		canvas.drawRect(startX, barHeigth, progress, height, paintRectBar);
		/**
		 * ���ƽ���������
		 */
		paintLineBar.setStyle(Paint.Style.FILL);
		paintLineBar.setAntiAlias(true);// �������
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
	 * ���ý��ȵ����ֵ
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
	 * ���ý��ȣ���Ϊ�̰߳�ȫ�ؼ������ڿ��Ƕ��ߵ����⣬��Ҫͬ��
	 * ˢ�½������postInvalidate()���ڷ�UI�߳�ˢ��
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
     * �϶����϶��¼�
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
