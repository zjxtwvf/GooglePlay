package com.example.googlepaly.fragment;

import java.util.ArrayList;

import com.example.googlepaly.view.LoadPage;
import com.example.googlepaly.view.LoadPage.ResultState;
import com.example.googleplay.utils.UIUtils;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment{

	private LoadPage loadPage;
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		System.out.println("onCreateView   ");
		loadPage = new LoadPage(UIUtils.getContext()){
			@Override
			public View onSuccessLoadView() {
				return BaseFragment.this.onSuccessLoadView();
			}
			@Override
			public ResultState onLoad() {
				return BaseFragment.this.onLoad();
			}
		};
		
		return loadPage;
	}
	
	public void LoadData(){
		if(loadPage != null){
			loadPage.loadData();
		}
	}
	
	protected ResultState checkResult(Object obj){
		if(null != obj){
			if(obj instanceof ArrayList){
				ArrayList result = (ArrayList)obj;
				if(result.size() == 0){
					return ResultState.STATE_NULL;
				}else{
					return ResultState.STATE_SUCCESS;
				}
			}
		}

		return ResultState.STATE_ERROR;
	}
	
	public abstract View onSuccessLoadView();
	public abstract ResultState onLoad();
	
}
