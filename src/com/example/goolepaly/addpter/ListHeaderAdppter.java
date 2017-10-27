package com.example.goolepaly.addpter;

import java.util.ArrayList;

import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.utils.BitmapCacheUtils;
import com.example.googleplay.utils.BitmapHelper;
import com.example.googleplay.utils.UIUtils;
import com.lidroid.xutils.BitmapUtils;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ListHeaderAdppter extends PagerAdapter {

	private ArrayList<String> mData;
	private BitmapUtils mBitmapHelper;

	public ListHeaderAdppter(ArrayList<String> data) {
		mData = data;
		mBitmapHelper = BitmapHelper.getBitmapHelper();
	}

	@Override
	public int getCount() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		position = position % mData.size();

		String url = mData.get(position);

		ImageView view = new ImageView(UIUtils.getContext());
		view.setScaleType(ScaleType.FIT_XY);

		//mBitmapHelper.display(view, HttpHelper.URL + "image?name=" + url);
		BitmapCacheUtils.getInstance().display(view, HttpHelper.URL + "image?name=" + url);
		

		container.addView(view);

		return view;
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}

}
