package com.example.googlepaly.holder;

import android.view.View;

public abstract class BaseHolder<T> {
	
	protected T data;
	private View mRootView;
	
	public BaseHolder(){
		mRootView = initView();
		mRootView.setTag(this);
	}
	
	public View getRootView(){
		return mRootView;
	}
	
	public void setData(T data){
		this.data = data;
		refreshView();
	}
	
    public abstract View initView();
    
    public abstract void refreshView();
}
