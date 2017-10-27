package com.example.goolepaly.addpter;

import java.util.ArrayList;

import com.example.googlepaly.holder.BaseHolder;
import com.example.googlepaly.holder.MoreHolder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class BaseAddpter<T> extends BaseAdapter{
	
	private static final int TYPE_MORE = 0;
	private static final int TYPE_NORMAL = 1;

	public ArrayList<T> data;
	
	public BaseAddpter(ArrayList<T> data){
		System.out.println("data  "+ data.size());
		this.data = data;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		if(position == data.size()-1){
			return TYPE_MORE;
		}
		return getInnerType(position);
	}
	
	public int getInnerType(int position){
		return TYPE_NORMAL;
	}
	
	public boolean hashMore(){
		return false;
	}

	@Override
	public View getView(int index, View convertView, ViewGroup arg2) {
		BaseHolder hoder = null;
		if(convertView == null){
			if(getItemViewType(index) == TYPE_MORE){
				hoder = new MoreHolder(hashMore());
			}else{
				hoder = getHoder(index);
			}
			
		}else{
			hoder = (BaseHolder) convertView.getTag();
		}
		if(getItemViewType(index) != TYPE_MORE){
			hoder.setData(data.get(index));
		}
		
		return hoder.getRootView();
	}
	
	public abstract BaseHolder<T> getHoder(int position);

}
