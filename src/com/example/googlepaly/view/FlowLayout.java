package com.example.googlepaly.view;

import java.util.ArrayList;

import com.example.googleplay.utils.UIUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class FlowLayout extends ViewGroup {

	private int mCurrentUsed = 0;
	private int mGap = UIUtils.dip2px(10);
	private int maxLineNum = 17;
	private int mGapHeight = UIUtils.dip2px(10);
	private Line mLine;
	private ArrayList<Line> mLineList = new ArrayList<FlowLayout.Line>();

	@Override
	protected void onLayout(boolean arg0, int l, int t, int arg3, int arg4) {
		int left = l + getPaddingLeft();
		int top = t + getPaddingTop();
		
        for(int i=0;i<mLineList.size();i++){
        	Line line = mLineList.get(i);
        	
        	line.layout(left, top);
        	
        	top += line.mHeight + mGapHeight;
        	
        }
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FlowLayout(Context context) {
		super(context);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
				- getPaddingRight();
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop()
				- getPaddingBottom();

		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int childCount = getChildCount();

		if (mLine == null) {
			mLine = new Line();
		}

		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);

			int widthSpec = MeasureSpec.makeMeasureSpec(width,
					(widthMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST
							: widthMode));
			int heightSpec = MeasureSpec.makeMeasureSpec(height,
					(heightMode == MeasureSpec.EXACTLY ? MeasureSpec.AT_MOST
							: heightMode));

			int childViewWidth = childView.getMeasuredWidth();
			mCurrentUsed += childViewWidth;

			if (mCurrentUsed < width) {
				mLine.addView(childView);
				mCurrentUsed += mGap;
				if (mCurrentUsed > width) {
					if (!nextLine()) {
						break;
					}
				}
			} else {
				if (mLine.getChildCount() == 0) {
					mLine.addView(childView);
					if (!nextLine()) {
						break;
					}
				} else {
					if (!nextLine()) {
						break;
					}
					mLine.addView(childView);
					mCurrentUsed += childViewWidth + mGap;
				}
			}
		}

		if (mLine != null && mLine.getChildCount() != 0
				&& !mLineList.contains(mLine)) {
			mLineList.add(mLine);
		}

		int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
		int totalHeight = 0;

		for (int i = 0; i < mLineList.size(); i++) {
			totalHeight += mLineList.get(i).mHeight;
		}

		totalHeight += (mGapHeight * (mLineList.size() - 1));

		totalHeight += getPaddingTop() + getPaddingBottom();

		setMeasuredDimension(totalWidth, totalHeight);
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public boolean nextLine() {
		mLineList.add(mLine);
		if (maxLineNum > mLineList.size()) {
			mLine = new Line();
			mCurrentUsed = 0;
			return true;
		}

		return false;
	}

	class Line {
		private int mWidthTotal;
		private int mHeight;
		private ArrayList<View> mChildViews = new ArrayList<View>();

		public void addView(View child) {
			mChildViews.add(child);
			mWidthTotal += child.getMeasuredWidth();
			if (mHeight < child.getMeasuredHeight()) {
				mHeight = child.getMeasuredHeight();
			}
		}

		public int getChildCount() {
			return mLineList.size();
		}

		public void layout(int left, int top) {
			int totalWidth = getMeasuredWidth() - getPaddingLeft()
					- getPaddingRight();
			int unUsedWidth = totalWidth - mWidthTotal - (getChildCount() - 1)
					* mGap;

			if (unUsedWidth >= 0) {
				int space = (int)(unUsedWidth / getChildCount() + 0.5f);
                for(int i=0;i<getChildCount();i++){
                	View child = mChildViews.get(i);
                	
                	int width = child.getMeasuredWidth();
                	int height = child.getMeasuredHeight();
                	
                	width += space;
                	
                	int widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
                	int hSpeceight = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
                	
                	child.measure(widthSpec, hSpeceight);
                	
                    int topOffset = (mHeight - height) / 2;
                    
                    if(topOffset < 0){
                    	topOffset = 0;
                    }else{
                    	
                    }
                    
                    child.layout(left, top+topOffset, left+width, top + topOffset + height);
                    
                    left += width + mGap;
                }
			}else{
				View child = mChildViews.get(0);
				child.layout(left, top, left + child.getMeasuredWidth(), top + getMeasuredHeight());
			}
		}
	}

}
