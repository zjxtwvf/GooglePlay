package com.example.goolepaly.addpter;

import java.util.ArrayList;

import com.example.googlepaly.holder.AppHolder;
import com.example.googlepaly.holder.BaseHolder;
import com.example.goolepaly.domain.AppInfo;

public class AppAddpter extends BaseAddpter<AppInfo>{

	public AppAddpter(ArrayList<AppInfo> data) {
		super(data);
	}

	@Override
	public BaseHolder<AppInfo> getHoder(int position) {
		return new AppHolder();
	}
}
