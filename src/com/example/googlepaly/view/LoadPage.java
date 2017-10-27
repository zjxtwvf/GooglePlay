package com.example.googlepaly.view;

import com.example.googleplay.R;
import com.example.googleplay.utils.UIUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public abstract class LoadPage extends FrameLayout {
	private static final int STATE_LOADING = 0;
	private static final int STATE_LOADING_ERROR = 1;
	private static final int STATE_LOADING_NULL = 2;
	private static final int STATE_LOADING_SUCESS = 3;
	private static final int STATE_LOADING_UNDO = 4;

	private int mCurrentState = STATE_LOADING_ERROR;
	private View mLoadingPage = null;
	private View mLoadingPageError = null;
	private View mLoadingPageNull = null;
	private View mSuccessView;

	public LoadPage(Context context) {
		super(context);
		initView();
	}

	public LoadPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}

	public LoadPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public abstract View onSuccessLoadView();

	public abstract ResultState onLoad();

	public void showRightPage() {
		
		mLoadingPage
				.setVisibility((mCurrentState == STATE_LOADING || mCurrentState == STATE_LOADING_UNDO) ? View.VISIBLE
						: View.GONE);
		mLoadingPageError
				.setVisibility((mCurrentState == STATE_LOADING_ERROR) ? View.VISIBLE
						: View.GONE);
		mLoadingPageNull
				.setVisibility((mCurrentState == STATE_LOADING_NULL) ? View.VISIBLE
						: View.GONE);
		
		if (mCurrentState == STATE_LOADING_SUCESS) {
			if (mSuccessView == null) {
				mSuccessView = onSuccessLoadView();
				addView(mSuccessView);
			}
		}

		if (mSuccessView != null) {
			mSuccessView
					.setVisibility((mCurrentState == STATE_LOADING_SUCESS) ? View.VISIBLE
							: View.GONE);
		}
	}

	public void initView() {
		if (null == mLoadingPage) {
			mLoadingPage = UIUtils.inflate(R.layout.loading_page);
			addView(mLoadingPage);
		}

		if (null == mLoadingPageError) {
			mLoadingPageError = UIUtils.inflate(R.layout.load_page_error);
			addView(mLoadingPageError);
		}

		if (null == mLoadingPageNull) {
			mLoadingPageNull = UIUtils.inflate(R.layout.load_page_null);
			addView(mLoadingPageNull);
		}

		showRightPage();
	}

	public void loadData() {
		if (mCurrentState != STATE_LOADING) {
			mCurrentState = STATE_LOADING;
			new Thread(new Runnable() {
				@Override
				public void run() {
					final ResultState result = onLoad();
					UIUtils.runOnUIThread(new Runnable() {
						@Override
						public void run() {
							if (result != null) {
								mCurrentState = result.getState();
								showRightPage();
							}
						}
					});
				}
			}).start();
		}
	}

	public enum ResultState {
		STATE_SUCCESS(STATE_LOADING_SUCESS), STATE_ERROR(STATE_LOADING_ERROR), STATE_NULL(
				STATE_LOADING_NULL);

		int state;

		public int getState() {
			return state;
		}

		ResultState(int state) {
			this.state = state;
		}
	}
}
