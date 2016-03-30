package com.hyuan.diy.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.hyuan.diy.R;

/**
 * 画圆弧、进度条、拖动条以及播放的扇形
 * Created by Administrator on 2015/12/11.
 */
public class CircleView extends View
{
    /**
     * 进度条与边界的距离
     */
    private int edgeMargin;
    /**
     * 圆形进度条的半径
     */
    private int rotateRaduis;
    /**
     * 进度条与罗盘之间的距离
     */
    private static final int mMaring = 45;
    private Point centPoint = new Point();

    private Context mContext;
    /**
     * 定义画笔
     */
    private Paint mPaint;
    private Paint Gpaint;
    private Paint pPaint;
    private Paint lPaint;
    private RectF oval,oval01;
    /**
     * 圆环的宽度
     */
    private float roundWidth;
    /**
     * 是否可以拖动小球
     */
    private boolean isDraging;
    /**
     * 进度条上的小球位置
     */
    private Point mRockBollPosition = new Point();

    /**
     * 进度条已经播放的角度
     */
    private int playedRadian;
    /**
     * 进度条已经加载的角度
     */
    private int loadRadian;

    public CircleView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        this.mContext = context;
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getHeight();
        rotateRaduis = (width/5*1);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundProgressBar);
        //获取自定义属性和默认值
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, rotateRaduis/20);
        mTypedArray.recycle();
        initValues();
    }

    /**
     * 设置画笔以及属性
     */
    private void initValues()
    {
        edgeMargin = 10;
        mPaint = new Paint();//画线的画笔
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(getResources().getColor(R.color.arcColor));
        centPoint.set(rotateRaduis + edgeMargin, rotateRaduis + edgeMargin);

        Gpaint = new Paint();//画进度条背景
        Gpaint.setColor(getResources().getColor(R.color.roundColor)); //设置圆环的颜色
        Gpaint.setStyle(Paint.Style.STROKE); //设置空心
        Gpaint.setStrokeWidth(roundWidth); //设置圆环的宽度
        Gpaint.setAntiAlias(true);  //消除锯齿

        pPaint = new Paint();//画进度条进度的画笔
        pPaint.setColor(getResources().getColor(R.color.roundProgressColor)); //设置圆环的颜色
        pPaint.setStyle(Paint.Style.STROKE); //设置空心
        pPaint.setStrokeWidth(roundWidth); //设置圆环的宽度
        pPaint.setAntiAlias(true);  //消除锯齿

        lPaint = new Paint();//画加载音乐的进度
        lPaint.setColor(getResources().getColor(R.color.loadProgressColor)); //设置圆环的颜色
        lPaint.setStyle(Paint.Style.STROKE); //设置空心
        lPaint.setStrokeWidth(roundWidth); //设置圆环的宽度
        lPaint.setAntiAlias(true);  //消除锯齿

        oval = new RectF((float)(centPoint.x-rotateRaduis),(float)(centPoint.y-rotateRaduis),
                (float)(centPoint.x + rotateRaduis),(float)(centPoint.y + rotateRaduis));
        oval01 = new RectF((centPoint.x-rotateRaduis + roundWidth/2),(centPoint.y-rotateRaduis + roundWidth/2),
                (centPoint.x + rotateRaduis - roundWidth/2),(centPoint.y + rotateRaduis - roundWidth/2));
    }

    /**
     * 绘画
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        int center = getWidth()/2;

        canvas.drawCircle(center, center, rotateRaduis, Gpaint); //画出圆环
        canvas.drawArc(oval, 270, loadRadian, false, lPaint);//进度条已经加载的角度

        canvas.drawArc(oval01,-90,playedRadian,true,mPaint);//进度条已经播放的扇形
        canvas.drawArc(oval, 270, playedRadian, false, pPaint);//进度条已经播放的角度

        //canvas.drawLine(centPoint.x,centPoint.y,mRockBollPosition.x, mRockBollPosition.y, mPaint);//画线
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
    {
        setMeasuredDimension(rotateRaduis * 2 + edgeMargin * 2, rotateRaduis * 2 + edgeMargin * 2);
    }

    /**
     * 拖动条拖动事件
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        Point cp ;
        Point rp ;
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                Point np = new Point((int)event.getX(), (int)event.getY());
                int distance = distance(centPoint ,np);
                cp = new Point((int)event.getX(), (int)event.getY());
                rp = getOnRadius(cp);
                if(distance >= rotateRaduis - mMaring && distance <= rotateRaduis + mMaring)
                {
                    mRockBollPosition.set(rp.x,rp.y);
                    isDraging = true;
                    flushRadian(centPoint ,cp);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(isDraging)
                {
                    cp = new Point((int) event.getX(), (int) event.getY());
                    rp = getOnRadius(cp);
                    mRockBollPosition.set(rp.x, rp.y);

                    /**
                     * calc playedRadian.
                     */
                    flushRadian(centPoint, cp);
                   // mActionMoveListener.onActionMove((float) playedRadian / 360f);
                }
                break;
            case MotionEvent.ACTION_UP:
                isDraging = false;
                if (null != mActionUpListener)
                {
                    mActionUpListener.onActionUp((float) playedRadian / 360f);
                }
                break;
            default:
                break;
        }
        postInvalidate();
        return true;
    }
    private Point getOnRadius(Point cp)
    {
        float radian = getRadian(cp, centPoint);
        int cX = centPoint.x + (int) (rotateRaduis * Math.cos(radian));
        int cY = centPoint.y - (int) (rotateRaduis * Math.sin(radian));
        return new Point(cX, cY);
    }
    private void flushRadian(Point cp, Point pp)
    {
        float lenA = pp.x - cp.x;
        float lenB = cp.y - pp.y;

        double atan = Math.atan(lenA/lenB);
        double radian = (atan/Math.PI)*180;
        if(pp.x>cp.x)
        {
            if(radian>0)
            {
                playedRadian = (int) radian;
            }else
            {
                playedRadian = (int) (180+radian);
            }
        }else
        {
            if(radian>=0)
            {
                playedRadian = (int) (180+radian);
            }else
            {
                playedRadian = (int) (360+radian);
            }
        }

    }

    private int distance(Point cp, Point np)
    {
        try
        {
            int dx = np.x-cp.x;
            int dy = np.y -cp.y;
            return (int) Math.sqrt(Math.abs(dx*dx)+Math.abs(dy*dy));
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * @describe 触摸点与中心点之间直线与水平方向的夹角角
     * @param a
     * @param b
     * @return
     */
    public static float getRadian(Point a, Point b)
    {
        float lenA = a.x - b.x;
        float lenB = a.y - b.y;
        float lenC = (float) Math.sqrt(lenA * lenA + lenB * lenB);
        float ang = (float) Math.acos(lenA / lenC);
        ang = ang * (b.y < a.y ? -1 : 1);
        return ang;
    }
    /**
     * 根据进度条角度刷新小球的位置
     *
     * @param
     */
    private void flushRockPosition(int ang)
    {
        mRockBollPosition.y = (int) (centPoint.y - (Math.cos(((float)ang/180f)*Math.PI)*rotateRaduis));
        mRockBollPosition.x = (int) (centPoint.x + (Math.sin(((float)ang/180f)*Math.PI)*rotateRaduis));
//		Log.i(TAG, "角度ang="+ang);
//		Log.i(TAG, "小球位置x="+mRockBollPosition.x+",y="+mRockBollPosition.y);
    }

    /**
     * 设置进度条进度
     * @param progress
     */
    public void setProgress(float progress)
    {
        playedRadian = (int) (360*progress);
        flushRockPosition(playedRadian);
        postInvalidate();
    }

    /**
     * 设置音乐加载的进度
     * @param progress
     */
    public void setLoadProgress(float progress)
    {
        loadRadian = (int) (360*progress);
        postInvalidate();
    }

    /**
     * Key move callback.
     */
    private onActionMoveListener mActionMoveListener;
    public void setActionMoveListener(onActionMoveListener actionMoveListener)
    {
        this.mActionMoveListener = actionMoveListener;
    }
    public interface onActionMoveListener
    {
        void onActionMove(float progress);
    }


    /**
     * Key up callback.
     */
    private onActionUpListener mActionUpListener;
    public void setActionUpListener(onActionUpListener actionUpListener)
    {
        this.mActionUpListener = actionUpListener;
    }
    public interface onActionUpListener
    {
        void onActionUp(float progress);
    }


    public boolean isDraging()
    {
        return isDraging;
    }

}

