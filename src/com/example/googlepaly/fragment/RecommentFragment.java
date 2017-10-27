package com.example.googlepaly.fragment;

import java.util.ArrayList;
import java.util.Random;

import com.example.googlepaly.view.StellarMap;
import com.example.googlepaly.view.LoadPage.ResultState;
import com.example.googleplay.utils.UIUtils;
import com.example.goolepaly.addpter.RecommentAdppter;
import com.example.goolepaly.domain.AppInfo;
import com.example.goolepaly.proctol.RecommendProctocol;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


public class RecommentFragment extends BaseFragment{
	
	private ArrayList<String>  mData;

	@Override
	public View onSuccessLoadView() {
		StellarMap stellarMap = new StellarMap(UIUtils.getContext());
		for(int i=0;i<mData.size();i++){
			TextView tView = new TextView(UIUtils.getContext());
			tView.setText(mData.get(i));
			tView.setTextSize(25);
			Random random = new Random();
			int r = 30 + random.nextInt(200);
			int g = 30 + random.nextInt(200);
			int b = 30 + random.nextInt(200);
			tView.setTextColor(Color.rgb(r, g, g));
			stellarMap.addView(tView);
		}
		TextView textView = new TextView(UIUtils.getContext()); 
		return textView;
	}

	@Override
	public ResultState onLoad() {
		RecommendProctocol reProctocol = new RecommendProctocol();
		mData  = reProctocol.getData(0);
		return checkResult(mData);
	}

}
