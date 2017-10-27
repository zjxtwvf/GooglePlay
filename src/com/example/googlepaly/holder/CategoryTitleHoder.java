package com.example.googlepaly.holder;

import com.example.googleplay.R;
import com.example.googleplay.utils.UIUtils;
import com.example.goolepaly.domain.CatogoryInfo;

import android.view.View;
import android.widget.TextView;

public class CategoryTitleHoder extends BaseHolder<CatogoryInfo>{
	
	private TextView mTextView;

	@Override
	public View initView() {
		View view = UIUtils.inflate(R.layout.list_item_category_title);
		mTextView = (TextView) view.findViewById(R.id.tv_title);
		return view;
	}

	@Override
	public void refreshView() {
		mTextView.setText(data.title);
	}

}
