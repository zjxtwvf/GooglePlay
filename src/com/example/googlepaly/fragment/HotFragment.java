package com.example.googlepaly.fragment;

import java.util.ArrayList;
import java.util.Random;

import com.example.googlepaly.view.MyFlowLayout02;
import com.example.googlepaly.view.MyFlowLayout;
import com.example.googlepaly.view.LoadPage.ResultState;
import com.example.googleplay.utils.DrawableUtils;
import com.example.googleplay.utils.UIUtils;
import com.example.goolepaly.proctol.HotProctol;

import android.graphics.Color;
import android.graphics.drawable.StateListDrawable;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;


public class HotFragment extends BaseFragment{

	private ArrayList<String> mData;
	
	@SuppressWarnings("deprecation")
	@Override
	public View onSuccessLoadView() {
		ScrollView scrollView = new ScrollView(UIUtils.getContext());
		MyFlowLayout flowLayout = new MyFlowLayout(UIUtils.getContext());
		int padding = UIUtils.dip2px(8);
		for(int i=0;i<mData.size();i++){
			TextView textView = new TextView(UIUtils.getContext());
			textView.setText(mData.get(i));
			textView.setTextSize(20);
			textView.setTextColor(Color.WHITE);
			textView.setPadding(padding, padding, padding, padding);
			
			Random random = new Random();
			int r = 30 + random.nextInt(200);
			int g = 30 + random.nextInt(200);
			int b = 30 + random.nextInt(200);

			int color = 0xffcecece;// 按下后偏白的背景色

			StateListDrawable selector = DrawableUtils.getSelector(
					Color.rgb(r, g, b), color, UIUtils.dip2px(6));
			textView.setBackgroundDrawable(selector);
			flowLayout.addView(textView);
		}
		flowLayout.setPadding(padding, padding, padding, padding);
		scrollView.addView(flowLayout);
		return scrollView;
	}

	@Override
	public ResultState onLoad() {
		HotProctol proctol = new HotProctol();
		mData = proctol.getData(0);
		return checkResult(mData);
	}

}
