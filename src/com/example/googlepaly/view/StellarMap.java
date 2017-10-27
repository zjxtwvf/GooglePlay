package com.example.googlepaly.view;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

public class StellarMap extends ViewGroup{
	
	private ArrayList<View> mViews = new ArrayList<View>();
	ArrayList<UsedSpace> mUsedSpace = new ArrayList<StellarMap.UsedSpace>();
	private int mHeight;
	private int mWidth;

	public StellarMap(Context context) {
		super(context);
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		int left = arg1;
		int top = arg2;
		
		for(int i=0;i<mViews.size();i++){
			Random random = new Random();
			int randomLeft;
			int randomTop;
			View view = mViews.get(i);
			while(true){
				randomLeft = left + random.nextInt(mWidth);
				randomTop = top + random.nextInt(mHeight);
				if(checkValid(randomLeft,randomTop,view.getMeasuredWidth(),view.getMeasuredHeight())){
					break;
				}
			}
			UsedSpace space = new UsedSpace();
			space.startX = randomLeft;
			space.endX = randomLeft + view.getMeasuredWidth();
			space.endY = randomTop;
			space.startY = randomTop + view.getMeasuredHeight();
			mUsedSpace.add(space);
			view.layout(randomLeft, randomTop, randomLeft + view.getMeasuredWidth(), randomTop + view.getMeasuredHeight());
		}
	}
	
	private boolean checkValid(int left,int top,int width,int height) {
		for(int i=0;i<getCnt();i++){
			if((Math.abs(left - mUsedSpace.get(i).endX)) <  width + Math.abs(mUsedSpace.get(i).endX - mUsedSpace.get(i).startX)){
				if((Math.abs(top - mUsedSpace.get(i).endY)) <  height + Math.abs(mUsedSpace.get(i).endY - mUsedSpace.get(i).startY)){
					return false;
				}
			}
		}
		return true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		mWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
	    mHeight = MeasureSpec.getSize(heightMeasureSpec) -getPaddingTop() -getPaddingBottom();
		
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		
		int ChildCnt = getChildCount();
		
		for(int i=0;i<ChildCnt;i++){
			int widthSpec = MeasureSpec.makeMeasureSpec(mWidth, ((widthMode == MeasureSpec.EXACTLY)?MeasureSpec.AT_MOST:widthMode));
			int heightSpec = MeasureSpec.makeMeasureSpec(mHeight, ((heightMode == MeasureSpec.EXACTLY)?MeasureSpec.AT_MOST:widthMode));
			
			View view = getChildAt(i);
			
			view.measure(widthSpec, heightSpec);
			
			mViews.add(view);
		}
		
		setMeasuredDimension(mWidth+getPaddingLeft()+getPaddingRight(), mHeight+getPaddingTop()+getPaddingBottom());
	}
	
	
	class UsedSpace{
		public int startX;
		public int endX;
		public int startY;
		public int endY;
	}
	
	public void addUsedSpace(UsedSpace us){
		mUsedSpace.add(us);
	}
	
	public int getCnt(){
		return mUsedSpace.size();
	}
}
