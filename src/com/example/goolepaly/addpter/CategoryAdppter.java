package com.example.goolepaly.addpter;

import java.util.ArrayList;

import com.example.googlepaly.holder.BaseHolder;
import com.example.googlepaly.holder.CategoryHolder;
import com.example.googlepaly.holder.CategoryTitleHoder;
import com.example.goolepaly.domain.CatogoryInfo;

public class CategoryAdppter extends BaseAddpter<CatogoryInfo>{
	
	private static final int TYPE_NORMAL = 2;
	private static final int TYPE_TITLE = 1;

	public CategoryAdppter(ArrayList<CatogoryInfo> data) {
		super(data);
	}
	
	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount() + 1;
	}

	@Override
	public int getInnerType(int position) {
		if(data.get(position).isTtilte){
			return TYPE_TITLE;
		}else{
			return TYPE_NORMAL;
		}
	}

	@Override
	public BaseHolder<CatogoryInfo> getHoder(int position) {
		if(getItemViewType(position) == TYPE_NORMAL){
			return new CategoryHolder();
		}else{
			return new CategoryTitleHoder();
		}
	}
}
