package com.example.goolepaly.addpter;

import java.util.ArrayList;

import com.example.googlepaly.holder.BaseHolder;
import com.example.googlepaly.holder.RecommentHolder;
import com.example.goolepaly.domain.AppInfo;

public class RecommentAdppter extends BaseAddpter<String>{

	public RecommentAdppter(ArrayList<String> data) {
		super(data);
	}

	@Override
	public BaseHolder<String> getHoder(int position) {
		return new RecommentHolder();
	}
}
