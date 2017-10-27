package com.example.googlepaly.activity;

import com.example.googlepaly.fragment.BaseFragment;
import com.example.googlepaly.fragment.FragmentFactory;
import com.example.googlepaly.view.PagerTab;
import com.example.googleplay.R;
import com.example.googleplay.utils.UIUtils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.os.Bundle;


public class MainActivity extends BaseActivity {

	private String [] mTabArrays;
	private ViewPager viewPager;
	private PagerTab pagerTab;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }
    
    public void initView(){
    	viewPager = (ViewPager) findViewById(R.id.vp_viewpager);
    	pagerTab = (PagerTab) findViewById(R.id.pt_pagertab);
    }
    
    public void initData(){
    	viewPager.setAdapter(new MyAdppter(getSupportFragmentManager()));
    	pagerTab.setViewPager(viewPager);
    }
    
    public void initEvent(){
    	pagerTab.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				BaseFragment fragment = FragmentFactory.creatFragment(position);
				fragment.LoadData();
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
    }

    class MyAdppter extends FragmentPagerAdapter{

		public MyAdppter(FragmentManager fm) {
			super(fm);
			mTabArrays = UIUtils.getStringArray(R.array.tab_names);
		}
		
		@Override
		public CharSequence getPageTitle(int position) {
			return mTabArrays[position];
		}

		@Override
		public Fragment getItem(int position) {
			return FragmentFactory.creatFragment(position);
		}

		@Override
		public int getCount() {
			return mTabArrays.length;
		}
    }
}
