package com.example.goolepaly.addpter;

import java.util.ArrayList;

import com.example.googlepaly.holder.BaseHolder;
import com.example.googlepaly.holder.HomeHolder;
import com.example.goolepaly.domain.AppInfo;

public  class HomeAddpter extends BaseAddpter<AppInfo>{

	public HomeAddpter(ArrayList<AppInfo> data) {
		super(data);
	}

	@Override
	public BaseHolder<AppInfo> getHoder(int position) {
		return new HomeHolder();
	}
}
