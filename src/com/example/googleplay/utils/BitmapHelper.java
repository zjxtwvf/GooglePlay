package com.example.googleplay.utils;

import com.lidroid.xutils.BitmapUtils;

public class BitmapHelper {

	private static BitmapUtils mBitmapHelper = null;
	private static Object obj = new Object();

	public static BitmapUtils getBitmapHelper() {

		synchronized (obj) {
			if (null == mBitmapHelper) {
				mBitmapHelper = new BitmapUtils(UIUtils.getContext());
			}
		}
		return mBitmapHelper;
	}
}
