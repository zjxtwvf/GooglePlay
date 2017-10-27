package com.example.googlepaly.fragment;

import java.util.ArrayList;
import com.example.googlepaly.activity.AppDetailsActivity;
import com.example.googlepaly.view.LoadPage.ResultState;
import com.example.googleplay.R;
import com.example.googleplay.utils.UIUtils;
import com.example.goolepaly.addpter.HomeAddpter;
import com.example.goolepaly.addpter.ListHeaderAdppter;
import com.example.goolepaly.domain.AppInfo;
import com.example.goolepaly.proctol.HomeProctol;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class HomeFragment extends BaseFragment {

	ArrayList<AppInfo> mDataList = new ArrayList<AppInfo>();
	ArrayList<String> mPictures;
	View mlistHeader;
	private ViewPager mViewPger;
	private boolean mOnceFlag = false;

	@Override
	public View onSuccessLoadView() {
		ListView listView = new ListView(UIUtils.getContext());
		listView.setAdapter(new HomeAddpter(mDataList));
		mlistHeader = UIUtils.inflate(R.layout.list_home_header);
		mViewPger = (ViewPager) mlistHeader.findViewById(R.id.vp_list_header); 
		mViewPger.setAdapter(new ListHeaderAdppter(mPictures));
		listView.addHeaderView(mlistHeader);
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(UIUtils.getContext(), AppDetailsActivity.class);
				intent.putExtra("packageName", mDataList.get(arg2-1).packageName);
				startActivity(intent);
			}
		});
		startLoop();
		return listView;
	}

	private void startLoop() {
		if(mOnceFlag){
			return;
		}
		mOnceFlag = true;
	    final Handler handle = new Handler();
	    handle.postDelayed(new Runnable() {
			@Override
			public void run() {
				mViewPger.setCurrentItem(mViewPger.getCurrentItem()+1);
				handle.postDelayed(this, 3000);
			}
		}, 3000);
	}

	@Override
	public ResultState onLoad() {
		HomeProctol home = new HomeProctol();
		mDataList = home.getData(0);
		mPictures = home.getPictures();
		return checkResult(mDataList);
	}
}
