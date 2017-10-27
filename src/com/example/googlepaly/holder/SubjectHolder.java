package com.example.googlepaly.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.googleplay.R;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.BitmapCacheUtils;
import com.example.googleplay.utils.BitmapHelper;
import com.example.googleplay.utils.UIUtils;
import com.example.goolepaly.domain.SubjectInfo;

public class SubjectHolder extends BaseHolder<SubjectInfo>{

	private ImageView mSubjectImage;
	private TextView mSubjectText;
	
	
	@Override
	public View initView() {
		View subjectView = UIUtils.inflate(R.layout.list_subject_item);
		mSubjectImage = (ImageView) subjectView.findViewById(R.id.iv_subject_item);
		mSubjectText = (TextView) subjectView.findViewById(R.id.tv_subject_item);
		return subjectView;
	}

	@Override
	public void refreshView() {
		mSubjectText.setText(data.des);
		//BitmapHelper.getBitmapHelper().display(mSubjectImage, HttpHelper.URL + "image?name="
				//+ data.url);
		BitmapCacheUtils.getInstance().display(mSubjectImage, HttpHelper.URL + "image?name="
				+ data.url);
	}
}
