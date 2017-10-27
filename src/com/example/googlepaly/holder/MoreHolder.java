package com.example.googlepaly.holder;

import com.example.googleplay.R;
import com.example.googleplay.utils.UIUtils;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MoreHolder extends BaseHolder<Integer> {
	
	public MoreHolder(boolean hashMore){
		setData(hashMore?STATE_LOAD_MORE:STATE_LOAD_NULL);
	}
	
	private static final int STATE_LOAD_MORE = 1;
	private static final int STATE_LOAD_ERROR = 2;
	private static final int STATE_LOAD_NULL = 3;
	private LinearLayout mRefreshItemView;
	private TextView mTextView;
	
	@Override
	public View initView() {
		View mItemView = UIUtils.inflate(R.layout.list_item_more_view);
		mRefreshItemView = (LinearLayout) mItemView.findViewById(R.id.ll_list_more_item);
		mTextView = (TextView) mItemView.findViewById(R.id.tv_list_more_view_error);
		return mItemView;
	}

	@Override
	public void refreshView(){
		switch(data){
			case STATE_LOAD_MORE:
				mRefreshItemView.setVisibility(View.INVISIBLE);
				mTextView.setVisibility(View.GONE);
				break;
			case STATE_LOAD_ERROR:
				mRefreshItemView.setVisibility(View.GONE);
				mTextView.setVisibility(View.INVISIBLE);
				break;
			case STATE_LOAD_NULL:
				mRefreshItemView.setVisibility(View.GONE);
				mTextView.setVisibility(View.GONE);
				break;
		}
	}
}
