package com.example.googleplay.utils;


import com.example.googlepaly.global.MyApplication;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

public class UIUtils{
	public static Context getContext(){
		return MyApplication.getContext();
	}
	
	public static Handler getHandler(){
		return MyApplication.getHandler();
	}
	
	public static String getString(int id){
		return getContext().getResources().getString(id);
	}
	
	public static String [] getStringArray(int id){
		return getContext().getResources().getStringArray(id);
	}
	
	public static Drawable getDrawable(int id){
		return getContext().getResources().getDrawable(id);
	}
	
	public static int  getColor(int id){
		return getContext().getResources().getColor(id);
	}
	
	public static int getDimen(int id){
		return getContext().getResources().getDimensionPixelSize(id);
	}
	
	public static int dip2px(float dip) {
		float density = UIUtils.getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * density + 0.5f);
	}

	public static float px2dip(int px) {
		float density = UIUtils.getContext().getResources().getDisplayMetrics().density;
		return px / density;
	}
	
	public static View inflate(int id){
		return View.inflate(getContext(), id, null);
	}
	
	public static void runOnUIThread(Runnable runnable){
		getHandler().post(runnable);
	}

	public static ColorStateList getColorStateList(int mTabTextColorResId) {
		return getContext().getResources().getColorStateList(mTabTextColorResId);
		//return null;
	}

	public static Boolean isRunOnUIThread() {
		// 获取当前线程id, 如果当前线程id和主线程id相同, 那么当前就是主线程
		int myTid = android.os.Process.myTid();
		if (myTid == getMainThreadId()) {
			return true;
		}
		return false;
	}
	
	public static int getMainThreadId() {
		return MyApplication.getMainThreadId();
	}
}
