package com.example.googlepaly.view;

import com.example.googleplay.utils.UIUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class MyProgressArc extends View{
	
	private Paint mPaint; //画笔
	private Drawable mBackGroundDraw;  //背景
	private int mProgressColor;   //进度条颜色 
	private int mDiameter;     //半径
	private int mSmoothTime = 1000;   //平滑时间
	private int mStrokeWidth;    //进度宽度
	private int mTextSize;
	private RectF mRectFArc = new RectF();
	
	private float mStartProgress;
	private float mProgress;
	private float mCurrentProgress;
	
	private int mStyle;
	
	private long mStartTime;
	private long mEndTime;
	
	public final static int PROGRESS_STYLE_NO_PROGRESS = -1;
	public final static int PROGRESS_STYLE_DOWNLOADING = 0;
	public final static int PROGRESS_STYLE_WAITING = 1;

	
	public MyProgressArc(Context context) {
		super(context);
		mPaint = new Paint();
		mPaint.setStrokeWidth(UIUtils.dip2px(1));
		mPaint.setStyle(Style.STROKE);
		mPaint.setAntiAlias(true);
		mPaint.setTextSize(30);
	}

	public MyProgressArc(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyProgressArc(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	public void setBackGround(Drawable drawable){
		mBackGroundDraw = drawable;
	}
	
	public void setStyle(int style){
		mStyle = style;
		invalidDateSafe();
	}
	
	public void setBackgroundResource(int id){
		mBackGroundDraw = UIUtils.getDrawable(id);
	}
	
	public void setArcDiameter(int diameter){
		mDiameter = diameter;
	}
	
	public void setProgress(float progress,Boolean smooth){
		mProgress = progress;
		if(smooth){
			mEndTime = mSmoothTime;
		}else{
			mEndTime = 0;
		}
		mStartTime = System.currentTimeMillis();
		mStartProgress = mCurrentProgress;
		invalidDateSafe();
	}
	
	public void invalidDateSafe(){
		if(UIUtils.isRunOnUIThread()){
			invalidate();
		}else{
			postInvalidate();
		}
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		int widthArc = 0;
		int heightArc = 0;
		
		if(widthMode == MeasureSpec.EXACTLY){
			widthArc = width;
		}else{
			if(null != mBackGroundDraw){
				widthArc = mBackGroundDraw.getIntrinsicWidth();
			}else{
				widthArc = 0;
			}
			if(widthMode == MeasureSpec.AT_MOST){
				widthArc = Math.min(widthArc, width);
			}
		}
		
		if(heightMode == MeasureSpec.EXACTLY){
			heightArc = height;
		}else{
			if(null != mBackGroundDraw){
				heightArc = mBackGroundDraw.getIntrinsicHeight();
			}else{
				heightArc = 0;
			}
			if(widthMode == MeasureSpec.AT_MOST){
				heightArc = Math.min(heightArc, height);
			}
		}
		
		mRectFArc.left = (widthArc - mDiameter)*0.5f;
		mRectFArc.top = (heightArc - mDiameter)*0.5f;
		mRectFArc.right = (widthArc + mDiameter)*0.5f;
		mRectFArc.bottom = (heightArc + mDiameter)*0.5f;
		
		setMeasuredDimension(widthArc, heightArc);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {		
		float factor;
		//画背景
		if(null != mBackGroundDraw){
			Rect rect = new Rect(0, 0, getMeasuredWidth(),getMeasuredHeight());
			mBackGroundDraw.setBounds(rect);
			mBackGroundDraw.draw(canvas);
		}

		if(mStyle != PROGRESS_STYLE_DOWNLOADING){
			return;
		}
		//画进度
		mPaint.setColor(mProgressColor);
		if(mProgress == 0){
			factor = 0;
			mCurrentProgress = 0;
		}else if(System.currentTimeMillis() - mStartTime < mEndTime){ //绘画时间还没有到
			factor = (System.currentTimeMillis() - mStartTime)/(float)mEndTime;
			mCurrentProgress += factor*(mProgress - mStartProgress);
		}else{
			factor = 1;
			mCurrentProgress = mProgress;
		}

		float sweep = mCurrentProgress * 360f;
		canvas.drawArc(mRectFArc, -90, sweep, false, mPaint);
		//平滑没有结束
		if(factor < 1 && factor > 0){
			invalidDateSafe();
		}
	}

	public void setProgressColor(int blue) {
		mProgressColor = blue;
	}
}
