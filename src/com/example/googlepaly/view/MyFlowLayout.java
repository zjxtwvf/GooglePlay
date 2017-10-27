package com.example.googlepaly.view;

import java.util.ArrayList;
import com.example.googleplay.utils.UIUtils;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyFlowLayout extends ViewGroup{
	public static final int WIDTHSPACING = UIUtils.dip2px(6);
	public static final int HEIGHTSPACING = UIUtils.dip2px(8);
	private int mUsedWidth;
	private ArrayList<Line> mlistlines = new ArrayList<MyFlowLayout.Line>();
	private Line mLine;

	public MyFlowLayout(Context context) {
		super(context);
	}

	public MyFlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyFlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		//获取控件有效宽高
		int width =  MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int childCnt = getChildCount();
		
		mLine = new Line();
		for(int i=0;i<childCnt;i++){
			View child = getChildAt(i);
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width, ((widthMode == MeasureSpec.EXACTLY)?MeasureSpec.AT_MOST:widthMode));
		    int childheightMeasureSpec = MeasureSpec.makeMeasureSpec(height, ((heightMode == MeasureSpec.EXACTLY)?MeasureSpec.AT_MOST:heightMode));
			
		    child.measure(childWidthMeasureSpec, childheightMeasureSpec);
			
			int childWidth = child.getMeasuredWidth();
			
			if(0 == mUsedWidth && childWidth > width){
				mLine.addView(child);
				mlistlines.add(mLine);
				mLine = new Line();
			}
			if(mUsedWidth + childWidth > width){
				mlistlines.add(mLine);
				mLine = new Line();
				mLine.addView(child);
				mUsedWidth += childWidth;
			}else{
				mUsedWidth += childWidth;
				if(mUsedWidth + WIDTHSPACING > width){
					mlistlines.add(mLine);
					mLine = new Line();
					mLine.addView(child);
					mUsedWidth += childWidth;
				}else{
					mUsedWidth += WIDTHSPACING;
					mLine.addView(child);	
				}
			}
		}
		//
		mlistlines.add(mLine);
		
		int totalWidth = MeasureSpec.getSize(widthMeasureSpec);
		int totalHeight = 0;
		
		for(int i=0;i<mlistlines.size();i++){
			totalHeight += mlistlines.get(i).mMaxHeight;
		}
		
		totalHeight += (mlistlines.size()-1)*HEIGHTSPACING;
		totalHeight += getPaddingTop() + getPaddingBottom();
		
		setMeasuredDimension(totalWidth,totalHeight);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		int leftTemp = left + getPaddingLeft(); 
		int topTemp = top + getPaddingTop();
		for(int i=0;i<mlistlines.size();i++){
			Line line = mlistlines.get(i);
			line.layoutChild(leftTemp, topTemp);
			topTemp += (line.mMaxHeight + HEIGHTSPACING);
		}
	}
	
	class Line{
		public int total;
		public int totalWidth;
		public int mMaxHeight;
		public ArrayList<View> mViews = new ArrayList<View>();
		
		public Line(){
		   mUsedWidth = 0;
		}
			
		public void addView(View view){
			total++;
			if(mMaxHeight < view.getMeasuredHeight()){
				mMaxHeight = view.getMeasuredHeight();
			}
			totalWidth += view.getMeasuredWidth();
			mViews.add(view);
			//
		}
		
		public void layoutChild(int left,int top){
			for(int i=0;i<total;i++){
				View view = mViews.get(i);
				TextView textView = (TextView)view;
				System.out.println("left" + left);
				System.out.println("top" + top);
				System.out.println("text  " + textView.getText());
				view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
				left += view.getMeasuredWidth() + WIDTHSPACING;
			}
		}
	}
}
