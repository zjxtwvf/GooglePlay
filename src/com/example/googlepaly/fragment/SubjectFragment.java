package com.example.googlepaly.fragment;

import java.util.ArrayList;

import com.example.googlepaly.view.LoadPage.ResultState;
import com.example.googleplay.utils.UIUtils;
import com.example.goolepaly.addpter.SubjectAdppter;
import com.example.goolepaly.domain.SubjectInfo;
import com.example.goolepaly.proctol.SubjectProctol;

import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;


public class SubjectFragment extends BaseFragment{

	ArrayList<SubjectInfo> mData;
	
	@Override
	public View onSuccessLoadView() {
		ListView listView = new ListView(UIUtils.getContext());
		listView.setAdapter(new SubjectAdppter(mData));
		return listView;
	}

	@Override
	public ResultState onLoad() {
		SubjectProctol subjectProctol = new SubjectProctol();
		mData = subjectProctol.getData(0);
		return checkResult(mData);
	}

}
