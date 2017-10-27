package com.example.goolepaly.addpter;

import java.util.ArrayList;

import com.example.googlepaly.holder.BaseHolder;
import com.example.googlepaly.holder.SubjectHolder;
import com.example.goolepaly.domain.SubjectInfo;

public class SubjectAdppter extends BaseAddpter<SubjectInfo>{

	public SubjectAdppter(ArrayList<SubjectInfo> data) {
		super(data);
	}

	@Override
	public BaseHolder<SubjectInfo> getHoder(int position) {
		return new SubjectHolder();
	}

}
