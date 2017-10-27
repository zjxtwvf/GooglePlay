package com.example.googlepaly.global;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

public class MyApplication extends Application{
	private static Context context;
	private static int mainThreadId;
	
	public static Context getContext() {
		return context;
	}

	public static Handler getHandler() {
		return handler;
	}

	private static Handler handler;
	
	@Override
	public void onCreate() {
		context = getApplicationContext();
		handler = new Handler();
		mainThreadId =  android.os.Process.myTid();
		super.onCreate();
	}

	public static int getMainThreadId() {
		return mainThreadId;
	}
}
