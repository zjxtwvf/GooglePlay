package com.example.googlepaly.fragment;

import java.util.ArrayList;

import com.example.googlepaly.view.LoadPage.ResultState;
import com.example.googleplay.utils.UIUtils;
import com.example.goolepaly.addpter.AppAddpter;
import com.example.goolepaly.domain.AppInfo;
import com.example.goolepaly.proctol.AppProctol;

import android.view.View;
import android.widget.ListView;


public class AppFragment extends BaseFragment{
	
	private ArrayList<AppInfo> mData;

	@Override
	public View onSuccessLoadView() {
		ListView appList = new ListView(UIUtils.getContext());
		appList.setAdapter(new AppAddpter(mData));
		return appList;
	}

	@Override
	public ResultState onLoad() {
		AppProctol appProctol = new AppProctol();
		mData = appProctol.getData(0);
		
		return checkResult(mData);
	}

}
