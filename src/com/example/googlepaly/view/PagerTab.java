package com.example.googlepaly.view;

import com.example.googlepaly.activity.BaseActivity;
import com.example.googleplay.R;
import com.example.googleplay.utils.UIUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.support.v4.widget.ScrollerCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.TextView;
/**
 * 1. ����BaseActivity�̳�ActionbarActivity(����v7��)
 * 2. ����״̬ѡ����bg_tab_text��tab_text_color
 * 3. �Զ�����
 * 4. ��UIUtils���ӷ���:getColorStateList
 * @author Kevin
 * @date 2015-10-27
 */
public class PagerTab extends ViewGroup {

	private ViewPager mViewPager;
	private PageListener mPageListener = new PageListener();//����ע���ViewPager����״̬�͹���
	private OnPageChangeListener mDelegatePageListener;//����֪ͨ���ViewPager��״̬�͹���
	private BaseActivity mActivity;

	private int mDividerPadding = 12;// �ָ������µ�padding
	private int mDividerWidth = 1;// �ָ��ߵĿ��
	private int mDividerColor = 0x1A000000;//�ָ�����ɫ
	private Paint mDividerPaint;//�ָ��ߵĻ���

	private int mIndicatorHeight = 4;//ָʾ���ĸ߶�
	private int mIndicatorWidth;//ָʾ���Ŀ�ȣ��Ƕ�̬������tab�Ŀ�ȱ仯
	private int mIndicatorLeft;//ָʾ���ľ�����ߵľ���
	private int mIndicatorColor = 0xFF0084FF;//ָʾ����ɫ
	private Paint mIndicatorPaint; //ָʾ���Ļ���

	private int mContentWidth;//��¼�������ݵĿ��
	private int mContentHeight;//��¼�������ݵĸ߶�

	private int mTabPadding = 24;// tab���ҵ��ڱ߾�
	private int mTabTextSize = 16; //tab���ִ�С
	private int mTabBackgroundResId = R.drawable.bg_tab_text;// tab������Դ
	private int mTabTextColorResId = R.color.tab_text_color; //tab������ɫ
	private int mTabCount;//tab�ĸ���

	private int mCurrentPosition = 0;//��ǰ���������tab���������Թ�����������ڵ�item��position
	private float mCurrentOffsetPixels;//�����߾��뵱ǰ���������tab����߾���
	private int mSelectedPosition = 0; //��ǰ��ѡ�е�tab�����ڼ�¼��ָ���tab��position

	private boolean mIsBeingDragged = false;//�Ƿ����϶���
	private float mLastMotionX;//��һ����ָ������x����
	private VelocityTracker mVelocityTracker;//���ڼ�¼�ٶȵİ�����
	private int mMinimumVelocity;//ϵͳĬ�ϵ���С����fling���ٶ�
	private int mMaximumVelocity;//ϵͳĬ������fling�ٶ�
	private int mTouchSlop;//ϵͳĬ�����㻬������Сλ��

	private ScrollerCompat mScroller;//��������İ�����
	private int mLastScrollX;//��¼��һ�ι�����xλ�ã��������ڴ���overScroll��ʵ��λ�ÿ��ܻ��ܵ�����

	private int mMaxScrollX = 0;// �ؼ����ɹ����ľ���
	private int mSplitScrollX = 0;// ����item�ĸ����������ÿ�ƶ�һ��item�ؼ���Ҫ�ƶ��ľ���

	private EdgeEffectCompat mLeftEdge;//����overScroll�ķ���Ч��
	private EdgeEffectCompat mRightEdge;

	public PagerTab(Context context) {
		this(context, null);
	}

	public PagerTab(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public PagerTab(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if (context instanceof BaseActivity) {
			mActivity = (BaseActivity) context;
		}
		init();
		initPaint();
	}

	/** ��ʼ��һЩ���� */
	private void init() {
		//��һ��ֵ��dipת����px
		mIndicatorHeight = UIUtils.dip2px(mIndicatorHeight);
		mDividerPadding = UIUtils.dip2px(mDividerPadding);
		mTabPadding = UIUtils.dip2px(mTabPadding);
		mDividerWidth = UIUtils.dip2px(mDividerWidth);
		mTabTextSize = UIUtils.dip2px(mTabTextSize);
		//����һ��scroller
		mScroller = ScrollerCompat.create(mActivity);
		//��ȡһ��ϵͳ����View�ĳ���������
		final ViewConfiguration configuration = ViewConfiguration.get(mActivity);
		//��ȡ��������С����
		mTouchSlop = configuration.getScaledTouchSlop();
		//��ȡfling����С�ٶ�
		mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
		//��ȡfling������ٶ�
		mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();

		mLeftEdge = new EdgeEffectCompat(mActivity);
		mRightEdge = new EdgeEffectCompat(mActivity);
	}

	/** ��ʼ���� */
	private void initPaint() {
		mIndicatorPaint = new Paint();
		mIndicatorPaint.setAntiAlias(true);
		mIndicatorPaint.setStyle(Paint.Style.FILL);
		mIndicatorPaint.setColor(mIndicatorColor);

		mDividerPaint = new Paint();
		mDividerPaint.setAntiAlias(true);
		mDividerPaint.setStrokeWidth(mDividerWidth);
		mDividerPaint.setColor(mDividerColor);
	}

	/** ����ViewPager */
	public void setViewPager(ViewPager viewPager) {
		if (viewPager == null || viewPager.getAdapter() == null) {
			throw new IllegalStateException("ViewPager is null or ViewPager does not have adapter instance.");
		}
		mViewPager = viewPager;
		onViewPagerChanged();
	}

	private void onViewPagerChanged() {
		mViewPager.setOnPageChangeListener(mPageListener);//��ViewPager���ü���
		mTabCount = mViewPager.getAdapter().getCount();//�ж��ٸ�tab��Ҫ��ViewPager�ж��ٸ�ҳ��
		for (int i = 0; i < mTabCount; i++) {
			if (mViewPager.getAdapter() instanceof IconTabProvider) {//�����Ҫʹ��icon��Ϊtab������Ҫadapterʵ��IconTabProvider�ӿ�
				addIconTab(i, ((IconTabProvider) mViewPager.getAdapter()).getPageIconResId(i));
			} else {
				addTextTab(i, mViewPager.getAdapter().getPageTitle(i).toString());
			}
		}
		ViewTreeObserver viewTreeObserver = getViewTreeObserver();
		if (viewTreeObserver != null) {//������һ����ȫ��layout�¼��������õ�ǰ��mCurrentPosition����ʾ��Ӧ��tab
			viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
				@Override
				public void onGlobalLayout() {
					getViewTreeObserver().removeGlobalOnLayoutListener(this);//ֻ��Ҫ����һ�Σ�֮��ͨ��listener�ص�����
					mCurrentPosition = mViewPager.getCurrentItem();
					if (mDelegatePageListener != null) {
						mDelegatePageListener.onPageSelected(mCurrentPosition);
					}
				}
			});
		}
	}

	/** ���ü�������ΪTab�����ViewPager��״̬�����Բ�Ҫ��ViewPager���ü����ˣ����ø�Tab����Tabת�� */
	public void setOnPageChangeListener(OnPageChangeListener listener) {
		mDelegatePageListener = listener;
	}

	/** �������tab */
	private void addTextTab(final int position, String title) {
		TextView tab = new TextView(mActivity);
		tab.setText(title);
		tab.setGravity(Gravity.CENTER);
		tab.setSingleLine();
		tab.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTabTextSize);
		tab.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		tab.setTextColor(UIUtils.getColorStateList(mTabTextColorResId));
		tab.setBackgroundDrawable(UIUtils.getDrawable(mTabBackgroundResId));
		tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		addTab(position, tab);
	}

	/** ���ͼƬicon */
	private void addIconTab(final int position, int resId) {
		ImageButton tab = new ImageButton(mActivity);
		tab.setImageResource(resId);
		tab.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		addTab(position, tab);
	}

	private void addTab(final int position, View tab) {
		tab.setFocusable(true);
		//����tab�ĵ���¼�����tab�����ʱ���л�pager��ҳ��
		tab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mViewPager.setCurrentItem(position);
			}
		});
		tab.setPadding(mTabPadding, 0, mTabPadding, 0);
		addView(tab, position);
	}

	/** ����ʱ�Ļص� */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// ��ȡ�ؼ�����Ŀ��,ģʽ
		int widthSize = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
		int heightSize = MeasureSpec.getSize(heightMeasureSpec) - getPaddingBottom() - getPaddingBottom();
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		int totalWidth = 0;
		int highest = 0;
		int goneChildCount = 0;
		for (int i = 0; i < mTabCount; i++) {
			final View child = getChildAt(i);
			if (child == null || child.getVisibility() == View.GONE) {
				goneChildCount--;
				continue;
			}
			int childWidthMeasureSpec;
			int childHeightMeasureSpec;

			LayoutParams childLayoutParams = child.getLayoutParams();
			if (childLayoutParams == null) {
				childLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			}

			if (childLayoutParams.width == LayoutParams.MATCH_PARENT) {
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
			} else if (childLayoutParams.width == LayoutParams.WRAP_CONTENT) {
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.AT_MOST);
			} else {
				childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.width, MeasureSpec.EXACTLY);
			}

			if (childLayoutParams.height == LayoutParams.MATCH_PARENT) {
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
			} else if (childLayoutParams.height == LayoutParams.WRAP_CONTENT) {
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.AT_MOST);
			} else {
				childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childLayoutParams.height, MeasureSpec.EXACTLY);
			}

			child.measure(childWidthMeasureSpec, childHeightMeasureSpec);

			int childWidth = child.getMeasuredWidth();
			int childHeight = child.getMeasuredHeight();

			totalWidth += childWidth;
			highest = highest < childHeight ? childHeight : highest;
		}

		if (totalWidth <= widthSize) {//�����Tab���ܿ��С��PagerTab�������ƽ��ģʽ
			int splitWidth = (int) (widthSize / (mTabCount - goneChildCount + 0.0f) + 0.5f);
			for (int i = 0; i < mTabCount; i++) {
				final View child = getChildAt(i);
				if (child == null || child.getVisibility() == View.GONE) {
					continue;
				}
				int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(splitWidth, MeasureSpec.EXACTLY);
				int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredHeight(), MeasureSpec.EXACTLY);
				child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
			}
			mMaxScrollX = 0;
			mSplitScrollX = 0;
		} else {//���������View���ڿؼ��Ŀ��
			mMaxScrollX = totalWidth - widthSize;
			mSplitScrollX = (int) (mMaxScrollX / (mTabCount - goneChildCount - 1.0f) + 0.5f);
		}

		if (widthMode == MeasureSpec.EXACTLY) {
			mContentWidth = widthSize;
		} else {
			mContentWidth = totalWidth;
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			mContentHeight = heightSize;
		} else {
			mContentHeight = highest;
		}

		int measureWidth = mContentWidth + getPaddingLeft() + getPaddingRight();
		int measureHeight = mContentHeight + getPaddingTop() + getPaddingBottom();
		setMeasuredDimension(measureWidth, measureHeight);
	}

	/** ����ʱ�Ļص� */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {//������ˣ�û�п���margin�����
		if (changed) {
			int height = b - t;//�ؼ�����View��ʾ�ĸ߶�
			int left = l;
			for (int i = 0; i < mTabCount; i++) {
				final View child = getChildAt(i);
				if (child == null || child.getVisibility() == View.GONE) {
					continue;
				}
				int top = (int) ((height - child.getMeasuredHeight()) / 2.0f + 0.5f);//����ؼ���tabҪ�ߣ��������ʾ
				int right = left + child.getMeasuredWidth();
				child.layout(left, top, right, top + child.getMeasuredHeight());//�ڷ�tab
				left = right;//��Ϊ��ˮƽ�ڷŵģ�����Ϊ��һ��׼��leftֵ
			}
		}
	}

	/** ����ʱ�Ļص� */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		final int height = getHeight();
		//��ָʾ��
		canvas.drawRect(mIndicatorLeft, height - mIndicatorHeight, mIndicatorLeft + mIndicatorWidth, height, mIndicatorPaint);

		// ���ָ���
		for (int i = 0; i < mTabCount - 1; i++) {//�ָ��ߵĸ�����tab�ĸ�����һ��
			final View child = getChildAt(i);
			if (child == null || child.getVisibility() == View.GONE) {
				continue;
			}
			if (child != null) {
				canvas.drawLine(child.getRight(), mDividerPadding, child.getRight(), mContentHeight - mDividerPadding, mDividerPaint);
			}
		}
		// ��ΪoverScrollЧ����һ������Ч����������Ҫ������
		boolean needsInvalidate = false;
		if (!mLeftEdge.isFinished()) {//���Ч��ûֹͣ
			final int restoreCount = canvas.save();//�ȱ��浱ǰ����
			final int heightEdge = getHeight() - getPaddingTop() - getPaddingBottom();
			final int widthEdge = getWidth();
			canvas.rotate(270);
			canvas.translate(-heightEdge + getPaddingTop(), 0);
			mLeftEdge.setSize(heightEdge, widthEdge);
			needsInvalidate |= mLeftEdge.draw(canvas);
			canvas.restoreToCount(restoreCount);
		}
		if (!mRightEdge.isFinished()) {
			final int restoreCount = canvas.save();
			final int widthEdge = getWidth();
			final int heightEdge = getHeight() - getPaddingTop() - getPaddingBottom();
			canvas.rotate(90);
			canvas.translate(-getPaddingTop(), -(widthEdge + mMaxScrollX));
			mRightEdge.setSize(heightEdge, widthEdge);
			needsInvalidate |= mRightEdge.draw(canvas);
			canvas.restoreToCount(restoreCount);
		}
		if (needsInvalidate) {
			postInvalidate();
		}
	}

	/** �����¼��Ƿ����صķ��� */
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		final int action = ev.getAction();
		if (mIsBeingDragged && action == MotionEvent.ACTION_MOVE) {//���Ѿ������϶������ҵ�ǰ�¼���MOVE��ֱ�����ѵ�
			return true;
		}
		switch (action) {
			case MotionEvent.ACTION_DOWN: {
				final float x = ev.getX();
				mLastMotionX = x; //��¼ס��ǰ��x����
				mIsBeingDragged = !mScroller.isFinished();//������µ�ʱ���ڹ��������״̬�����϶�״̬
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				final float x = ev.getX();
				final int xDiff = (int) Math.abs(x - mLastMotionX);//�������εĲ�ֵ
				if (xDiff > mTouchSlop) {//���������С�ƶ��ľ��룬���״̬�ı�Ϊ�϶�״̬
					mIsBeingDragged = true;
					mLastMotionX = x;
					ViewParent parent = getParent();//������View��Ҫ�������Լ������¼��������Լ�����
					if (parent != null) {
						parent.requestDisallowInterceptTouchEvent(true);
					}
				}
				break;
			}
			case MotionEvent.ACTION_CANCEL://����ָ�뿪���ߴ����¼�ȡ����ʱ�򣬰��϶�״̬ȡ����
			case MotionEvent.ACTION_UP:
				mIsBeingDragged = false;
				break;
		}
		return mIsBeingDragged;//������϶�״̬���������¼��������Լ���onTouch����
	}

	/** �����¼��Ĵ����� */
	public boolean onTouchEvent(MotionEvent ev) {
		if (mVelocityTracker == null) {
			mVelocityTracker = VelocityTracker.obtain();
		}
		mVelocityTracker.addMovement(ev);
		final int action = ev.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN: {//�����down�¼�����¼ס��ǰ��x����
				final float x = ev.getX();
				if (!mScroller.isFinished()) {
					mScroller.abortAnimation();
				}
				mLastMotionX = x;
				break;
			}
			case MotionEvent.ACTION_MOVE: {
				final float x = ev.getX();
				final float deltaX = x - mLastMotionX;
				if (!mIsBeingDragged) {//�����û�д����϶������ж����εĲ�ֵ�Ƿ������С�϶��ľ���
					if (Math.abs(deltaX) > mTouchSlop) {
						mIsBeingDragged = true;
					}
				}
				if (mIsBeingDragged) {//��������϶�״̬����¼סx����
					mLastMotionX = x;
					onMove(deltaX);
				}
				break;
			}
			case MotionEvent.ACTION_UP: {
				if (mIsBeingDragged) {
					final VelocityTracker velocityTracker = mVelocityTracker;
					//�ȶ��ٶȽ���һ����������һ��������ʱ�䵥λ��1000���룬�ڶ�������������ٶȡ�
					velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
					float velocity = velocityTracker.getXVelocity();//��ȡˮƽ�����ϵ��ٶ�
					onUp(velocity);
				}
			}
			case MotionEvent.ACTION_CANCEL: {
				mIsBeingDragged = false;
				if (mVelocityTracker != null) {
					mVelocityTracker.recycle();
					mVelocityTracker = null;
				}
				break;
			}
		}
		return true;
	}

	private void onMove(float x) {
		if (mMaxScrollX <= 0) {
			if (mViewPager.isFakeDragging() || mViewPager.beginFakeDrag()) {
				mViewPager.fakeDragBy(x);
			}
		} else {
			int scrollByX = -(int) (x + 0.5);
			if (getScrollX() + scrollByX < 0) {
				scrollByX = 0 - getScrollX();
				mLeftEdge.onPull(Math.abs(x) / getWidth());
			}
			if (getScrollX() + scrollByX > mMaxScrollX) {
				scrollByX = mMaxScrollX - getScrollX();
				mRightEdge.onPull(Math.abs(x) / getWidth());
			}
			scrollBy(scrollByX, 0);
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	private void onUp(float velocity) {
		if (mMaxScrollX <= 0) {
			if (mViewPager.isFakeDragging()) mViewPager.endFakeDrag();
		} else {
			if (Math.abs(velocity) <= mMinimumVelocity) {
				return;
			}
			mScroller.fling(getScrollX(), 0, -(int) (velocity + 0.5), 0, 0, mMaxScrollX, 0, 0, 270, 0);
			ViewCompat.postInvalidateOnAnimation(this);
		}
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			int oldX = mLastScrollX;
			mLastScrollX = mScroller.getCurrX();
			if (mLastScrollX < 0 && oldX >= 0) {
				mLeftEdge.onAbsorb((int) mScroller.getCurrVelocity());
			} else if (mLastScrollX > mMaxScrollX && oldX <= mMaxScrollX) {
				mRightEdge.onAbsorb((int) mScroller.getCurrVelocity());
			}
			int x = mLastScrollX;
			if (mLastScrollX < 0) {
				x = 0;
			} else if (mLastScrollX > mMaxScrollX) {
				x = mMaxScrollX;
			}
			scrollTo(x, 0);
		}
		ViewCompat.postInvalidateOnAnimation(this);
	}

	/** ���mIndicatorOffset�ĺϷ��ԣ�������������й�tab������ֵ */
	private void checkAndcalculate() {
		// ���ָʾ����ʼλ�ñȵ�һ��tab����ʼλ�û�ҪС������Ϊ��һ��tab����ʼλ�ã�ָʾ����Ⱦ��ǵ�һ��tab�Ŀ��
		final View firstTab = getChildAt(0);
		if (mIndicatorLeft < firstTab.getLeft()) {
			mIndicatorLeft = firstTab.getLeft();
			mIndicatorWidth = firstTab.getWidth();
		}
		// ���ָʾ����ʼλ�ñ����һ��tab����ʼλ�û�Ҫ�󣬾���Ϊ���һ��tab����ʼλ�ã�ָʾ����Ⱦ������һ��tab�Ŀ��
		View lastTab = getChildAt(mTabCount - 1);
		if (mIndicatorLeft > lastTab.getLeft()) {
			mIndicatorLeft = lastTab.getLeft();
			mIndicatorWidth = lastTab.getWidth();
		}
		// ͨ��ָʾ������ʼλ�ü������ǰ���ڵڼ���position�����Ҽ�����Ѿ�ƫ���˶��٣�ƫ�������Ե�ǰ������tab�Ŀ�ȵİٷֱ�
		for (int i = 0; i < mTabCount; i++) {
			View tab = getChildAt(i);
			if (mIndicatorLeft < tab.getLeft()) {
				mCurrentPosition = i - 1;
				View currentTab = getChildAt(mCurrentPosition);
				mCurrentOffsetPixels = (mIndicatorLeft - currentTab.getLeft()) / (currentTab.getWidth() + 0.0f);
				break;
			}
		}
	}

	/** ������ָ����child */
	public void scrollSelf(int position, float offset) {
		if (position >= mTabCount) {
			return;
		}
		final View tab = getChildAt(position);
		mIndicatorLeft = (int) (tab.getLeft() + tab.getWidth() * offset + 0.5);
		int rightPosition = position + 1;
		if (offset > 0 && rightPosition < mTabCount) {
			View rightTab = getChildAt(rightPosition);
			mIndicatorWidth = (int) (tab.getWidth() * (1 - offset) + rightTab.getWidth() * offset + 0.5);
		} else {
			mIndicatorWidth = tab.getWidth();
		}
		checkAndcalculate();

		int newScrollX = position * mSplitScrollX + (int) (offset * mSplitScrollX + 0.5);
		if (newScrollX < 0) {
			newScrollX = 0;
		}
		if (newScrollX > mMaxScrollX) {
			newScrollX = mMaxScrollX;
		}
		//scrollTo(newScrollX, 0);//����
		int duration = 100;
		if (mSelectedPosition != -1) {
			duration = (Math.abs(mSelectedPosition - position)) * 100;
		}
		mScroller.startScroll(getScrollX(), 0, (newScrollX - getScrollX()), 0, duration);
		ViewCompat.postInvalidateOnAnimation(this);
	}

	/** ѡ��ָ��λ�õ�Tab */
	private void selectTab(int position) {
		for (int i = 0; i < mTabCount; i++) {
			View tab = getChildAt(i);
			if (tab != null) {
				tab.setSelected(position == i);
			}
		}
	}

	/** ViewPager��OnPageChangeListenerʵ���࣬��Ϊ������Ҫ��PagerTab�л�ȡPagerView�ļ������Ա���Ե���tab */
	private class PageListener implements OnPageChangeListener {
		@Override
		public void onPageScrolled(int position, float positionOffset, final int positionOffsetPixels) {
			//����VierPager��ƫ��ֵ������tab
			scrollSelf(position, positionOffset);
			if (mDelegatePageListener != null) {//������ṩ���ⲿ��
				mDelegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
			}
		}

		@Override
		public void onPageScrollStateChanged(int state) {
			if (state == ViewPager.SCROLL_STATE_IDLE) {
				mSelectedPosition = -1;
			}
			if (mDelegatePageListener != null) {
				mDelegatePageListener.onPageScrollStateChanged(state);
			}
		}

		@Override
		public void onPageSelected(int position) {
			System.out.println("onPageSelected:" + position);
			mSelectedPosition = position;
			selectTab(position);
			if (mDelegatePageListener != null) {
				mDelegatePageListener.onPageSelected(position);
			}
		}
	}

	/** ���ָʾ��ϣ����ͼƬ����̳иýӿ� */
	public interface IconTabProvider {
		public int getPageIconResId(int position);
		public int getPageSelectedIconResId();
	}
}
