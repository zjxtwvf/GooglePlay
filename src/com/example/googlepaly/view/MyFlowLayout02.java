package com.example.googlepaly.view;

import java.util.ArrayList;

import com.example.googleplay.utils.UIUtils;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

public class MyFlowLayout02 extends ViewGroup {

	private int mUsedWidth;// 当前行已使用的宽�?
	private int mHorizontalSpacing = UIUtils.dip2px(6);// 水平间距
	private int mVerticalSpacing = UIUtils.dip2px(8);// 竖直间距

	private Line mLine;// 当前行对�?

	private ArrayList<Line> mLineList = new ArrayList<MyFlowLayout02.Line>();// 维护�?有行的集�?

	private static final int MAX_LINE = 100;// �?大行数是100�?

	public MyFlowLayout02(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MyFlowLayout02(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyFlowLayout02(Context context) {
		super(context);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int left = l + getPaddingLeft();
		int top = t + getPaddingTop();

		// 遍历�?有行对象, 设置每行位置
		for (int i = 0; i < mLineList.size(); i++) {
			Line line = mLineList.get(i);
			line.layout(left, top);
			top += line.mMaxHeight + mVerticalSpacing;// 更新top�?
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// 获取有效宽度
		int width = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
				- getPaddingRight();
		// 获取有效高度
		int height = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop()
				- getPaddingBottom();

		// 获取宽高模式
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int childCount = getChildCount();// 获取�?有子控件数量
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);

			// 如果父控件是确定模式, 子控件包裹内�?;否则子控件模式和父控件一�?
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(width,
					(widthMode == MeasureSpec.EXACTLY) ? MeasureSpec.AT_MOST
							: widthMode);
			int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height,
					(heightMode == MeasureSpec.EXACTLY) ? MeasureSpec.AT_MOST
							: heightMode);

			// �?始测�?
			childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

			// 如果当前行对象为�?, 初始化一个行对象
			if (mLine == null) {
				mLine = new Line();
			}

			// 获取子控件宽�?
			int childWidth = childView.getMeasuredWidth();

			mUsedWidth += childWidth;// 已使用宽度增加一个子控件宽度

			if (mUsedWidth < width) {// 判断是否超出了边�?
				mLine.addView(childView);// 更当前行对象添加子控�?

				mUsedWidth += mHorizontalSpacing;// 增加�?个水平间�?

				if (mUsedWidth > width) {
					// 增加水平间距之后, 就超出了边界, 此时�?要换�?
					if (!newLine()) {
						break;// 如果创建行失�?,就结束循�?,不再添加
					}
				}

			} else {
				// 已超出边�?
				// 1.当前没有任何控件, �?旦添加当前子控件, 就超出边�?(子控件很�?)
				if (mLine.getChildCount() == 0) {
					mLine.addView(childView);// 强制添加到当前行

					if (!newLine()) {// 换行
						break;
					}
				} else {
					// 2.当前有控�?, �?旦添�?, 超出边界
					if (!newLine()) {// 换行
						break;
					}

					mLine.addView(childView);
					mUsedWidth += childWidth + mHorizontalSpacing;// 更新已使用宽�?
				}
			}

		}

		// 保存�?后一行的行对�?
		if (mLine != null && mLine.getChildCount() != 0
				&& !mLineList.contains(mLine)) {
			mLineList.add(mLine);
		}

		int totalWidth = MeasureSpec.getSize(widthMeasureSpec);// 控件整体宽度

		int totalHeight = 0;// 控件整体高度
		for (int i = 0; i < mLineList.size(); i++) {
			Line line = mLineList.get(i);
			totalHeight += line.mMaxHeight;
		}

		totalHeight += (mLineList.size() - 1) * mVerticalSpacing;// 增加竖直间距
		totalHeight += getPaddingTop() + getPaddingBottom();// 增加上下边距

		// 根据�?新的宽高来测量整体布�?的大�?
		setMeasuredDimension(totalWidth, totalHeight);
		// super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	// 换行
	private boolean newLine() {
		mLineList.add(mLine);// 保存上一行数�?

		if (mLineList.size() < MAX_LINE) {
			// 可以继续添加
			mLine = new Line();
			mUsedWidth = 0;// 已使用宽度清�?

			return true;// 创建行成�?
		}

		return false;// 创建行失�?
	}

	// 每一行的对象封装
	class Line {

		private int mTotalWidth;// 当前�?有控件�?�宽�?
		public int mMaxHeight;// 当前控件的高�?(以最高的控件为准)

		private ArrayList<View> mChildViewList = new ArrayList<View>();// 当前行所有子控件集合

		// 添加�?个子控件
		public void addView(View view) {
			mChildViewList.add(view);
			// 总宽度增�?
			mTotalWidth += view.getMeasuredWidth();

			int height = view.getMeasuredHeight();
			// 0 10 20 10
			mMaxHeight = mMaxHeight < height ? height : mMaxHeight;
		}

		public int getChildCount() {
			return mChildViewList.size();
		}

		// 子控件位置设�?
		public void layout(int left, int top) {
			int childCount = getChildCount();

			// 将剩余空间分配给每个子控�?
			int validWidth = getMeasuredWidth() - getPaddingLeft()
					- getPaddingRight();// 屏幕总有效宽�?
			// 计算剩余宽度
			int surplusWidth = validWidth - mTotalWidth - (childCount - 1)
					* mHorizontalSpacing;

			if (surplusWidth >= 0) {
				// 有剩余空�?
				int space = (int) ((float) surplusWidth / childCount + 0.5f);// 平均每个控件分配的大�?

				// 重新测量子控�?
				for (int i = 0; i < childCount; i++) {
					View childView = mChildViewList.get(i);

					int measuredWidth = childView.getMeasuredWidth();
					int measuredHeight = childView.getMeasuredHeight();

					measuredWidth += space;// 宽度增加

					int widthMeasureSpec = MeasureSpec.makeMeasureSpec(
							measuredWidth, MeasureSpec.EXACTLY);
					int heightMeasureSpec = MeasureSpec.makeMeasureSpec(
							measuredHeight, MeasureSpec.EXACTLY);

					// 重新测量控件
					childView.measure(widthMeasureSpec, heightMeasureSpec);

					// 当控件比较矮�?,�?要居中展�?, 竖直方向�?要向下有�?定偏�?
					int topOffset = (mMaxHeight - measuredHeight) / 2;

					if (topOffset < 0) {
						topOffset = 0;
					}

					// 设置子控件位�?
					childView.layout(left, top + topOffset, left
							+ measuredWidth, top + topOffset + measuredHeight);
					left += measuredWidth + mHorizontalSpacing;// 更新left�?
				}

			} else {
				// 这个控件很长, 占满整行
				View childView = mChildViewList.get(0);
				childView.layout(left, top,
						left + childView.getMeasuredWidth(),
						top + childView.getMeasuredHeight());
			}

		}

	}

}
