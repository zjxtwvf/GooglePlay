package com.example.googlepaly.view;

import com.example.googleplay.manager.DownLoadManager;
import com.example.googleplay.utils.UIUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class MyHoriProgress extends View{
	
	private static final long SMOOTHTIME = 1000L; 
	
	private Drawable mBackGroundDraw;
	private Drawable mPregressDraw;
	
	private float mStartProgress;
	private float mCurrentProgress;
	private float mProgress;
	
	private long mStartTime;
	private long mEndTime;
	private int mTextColor;
	private int mTextSize;
	
	private int mStyle;
	
	private Rect mRectF = new Rect();
	private Paint mPaint;

	StringBuffer buffer = new StringBuffer();
	
	public void setProgressBackgroundResource(int id){
		mBackGroundDraw = UIUtils.getDrawable(id);
	}
	
	public void setProgressResource(int id){
		mPregressDraw = UIUtils.getDrawable(id);
	}
	
	public void setProgressTextColor(int color){
		mTextColor = color;
		mPaint.setColor(mTextColor);
	}
	
	public void setProgressTextSize(int textSize){
		mTextSize = textSize;
		mPaint.setTextSize(mTextSize);
		mPaint.setTextAlign(Align.CENTER);
	}

	public MyHoriProgress(Context context) {
		super(context);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
	}

	public MyHoriProgress(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	public MyHoriProgress(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
				
	public void setProgress(float progress,Boolean smooth){
		 if(progress == 0){
			 mProgress = 0;
		 }else{
			 mProgress = progress;
		 }
		 mStartTime = System.currentTimeMillis();
		 if(smooth){
			 mEndTime = mStartTime + SMOOTHTIME;
		 }else{
			 mEndTime = mStartTime;
		 }
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
		
		mRectF.top = 0;
		mRectF.left = 0;
		mRectF.right = widthArc;
		mRectF.bottom = heightArc;
		
		mBackGroundDraw.setBounds(0, 0, widthArc, heightArc);
		
		setMeasuredDimension(widthArc, heightArc);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {	
		float factor;
		if(mBackGroundDraw != null){
			mBackGroundDraw.draw(canvas);
		}
		
		if(mProgress == 1){
			factor = 1;
		}else if(System.currentTimeMillis() < mEndTime){
			factor = (System.currentTimeMillis() - mStartTime)/(float)SMOOTHTIME;
		}else{
			factor = 1;
		}
		
		mCurrentProgress = factor*(mProgress - mStartProgress) + mStartProgress; 
		mPregressDraw.setBounds(mRectF.left, mRectF.top, (int)(mRectF.right*mCurrentProgress), mRectF.bottom);
		mPregressDraw.draw(canvas);
		
		buffer.delete(0, buffer.length());
		if(mStyle == DownLoadManager.STATE_DOWNLOADING){
			buffer.append((int)(mCurrentProgress * 100) +"%");
		}else if(mStyle == DownLoadManager.STATE_PAUSE){
			buffer.append("暂停");
		}else if(mStyle == DownLoadManager.STATE_FAILED){
			buffer.append("下载失败");
		}else if(mStyle == DownLoadManager.STATE_SUCESS){
			buffer.append("安装");
		}else if(mStyle == DownLoadManager.STATE_UNDO){
			buffer.append("下载");
		}else{
			buffer.append("等待下载");
		}
		
		canvas.drawText(buffer.toString(), mRectF.right/2, mRectF.bottom/2+mTextSize/2, mPaint);
		
		if(factor != 1){
			invalidDateSafe();
		}
	}

	public void setStyle(int mCurrenState) {
        mStyle = mCurrenState;
        invalidDateSafe();
	}
}
