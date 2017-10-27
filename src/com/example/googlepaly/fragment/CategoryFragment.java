package com.example.googlepaly.fragment;

import java.util.ArrayList;

import com.example.googlepaly.view.LoadPage.ResultState;
import com.example.googleplay.utils.UIUtils;
import com.example.goolepaly.addpter.CategoryAdppter;
import com.example.goolepaly.domain.CatogoryInfo;
import com.example.goolepaly.proctol.CategoryProctol;

import android.view.View;
import android.widget.ListView;


public class CategoryFragment extends BaseFragment{

	private ArrayList<CatogoryInfo> mData;
	
	
	@Override
	public View onSuccessLoadView() {
		ListView listView = new ListView(UIUtils.getContext());
		listView.setAdapter(new CategoryAdppter(mData));
		return listView;
	}

	@Override
	public ResultState onLoad() {
		CategoryProctol proctol = new CategoryProctol();
		mData = proctol.getData(0);
		return checkResult(mData);
	}

}
