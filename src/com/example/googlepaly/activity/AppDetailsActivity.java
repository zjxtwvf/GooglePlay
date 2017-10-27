package com.example.googlepaly.activity;

import java.util.ArrayList;

import com.example.googlepaly.view.LoadPage;
import com.example.googlepaly.view.LoadPage.ResultState;
import com.example.googlepaly.view.MyHoriProgress;
import com.example.googleplay.R;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.manager.DownLoadManager;
import com.example.googleplay.manager.DownLoadManager.DownLoadAbserve;
import com.example.googleplay.utils.BitmapCacheUtils;
import com.example.googleplay.utils.UIUtils;
import com.example.goolepaly.domain.AppDetailsInfo;
import com.example.goolepaly.domain.AppInfo;
import com.example.goolepaly.domain.DownLoadInfo;
import com.example.goolepaly.domain.AppDetailsInfo.Safe;
import com.example.goolepaly.proctol.HomeDetailsProctol;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppDetailsActivity extends BaseActivity {
	
	private AppDetailsInfo mData;
	private String packageName;
	private Boolean mIsDown = true;
	private LinearLayout layoutSafe1;
	private ImageView safePull;
	private int mHeight;
	private RelativeLayout relativeLayout;
	private FrameLayout mFrameLayoutDownLoad;
	private DownLoadManager mDownLoadManager = DownLoadManager.getInstence();
	
	private int mCurrenState = DownLoadManager.STATE_UNDO;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LoadPage loadPage = new LoadPage(this) {
			@Override
			public View onSuccessLoadView() {
				return AppDetailsActivity.this.onSuccessLoadView();
			}
			@Override
			public ResultState onLoad() {
				return AppDetailsActivity.this.onLoad();
			}
		};
		packageName = getIntent().getStringExtra("packageName");
		
		loadPage.loadData();
		setContentView(loadPage);
	}

	private View onSuccessLoadView() {
		View view = UIUtils.inflate(R.layout.activity_app_details);
		ImageView appIcon = (ImageView) view.findViewById(R.id.iv_app_icon);
		safePull = (ImageView) view.findViewById(R.id.iv_safe_pull);
		TextView appName = (TextView) view.findViewById(R.id.tv_details_app_name);
		RatingBar ratingBar = (RatingBar) view.findViewById(R.id.rb_star_app_details);
		TextView downloadNum = (TextView) view.findViewById(R.id.tv_downloadnum);
		TextView date = (TextView) view.findViewById(R.id.tv_date);
		TextView version = (TextView) view.findViewById(R.id.tv_version);
		TextView size = (TextView) view.findViewById(R.id.tv_size);
		TextView des = (TextView) view.findViewById(R.id.tv_details_des);
		TextView author = (TextView) view.findViewById(R.id.tv_author);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.ll_app_details_hs);
		LinearLayout layoutSafe = (LinearLayout) view.findViewById(R.id.ll_safe_des);
		layoutSafe1 = (LinearLayout) view.findViewById(R.id.ll_safe1);
		relativeLayout = (RelativeLayout) view.findViewById(R.id.rl_safe_pull);
		mFrameLayoutDownLoad = (FrameLayout) view.findViewById(R.id.fl_safe_download);
		ratingBar.setRating(mData.stars);
		appName.setText(mData.name);
		
		BitmapCacheUtils.getInstance().display(appIcon, HttpHelper.URL + "image?name="+mData.iconUrl);
		des.setText(mData.des);
		version.setText(mData.version);
		date.setText(mData.date);
		downloadNum.setText("下载量："+mData.downloadNum);
		size.setText((mData.size/1024/1024)+"MB");
		author.setText(mData.author);
		
		
		ArrayList<String> pictures = mData.screen;
		LinearLayout.LayoutParams layoutParams = new LayoutParams(UIUtils.dip2px(90), UIUtils.dip2px(150));
		for(int i=0;i<pictures.size();i++){
			ImageView imageView = new ImageView(UIUtils.getContext());
			imageView.setLayoutParams(layoutParams);
			imageView.setPadding(UIUtils.dip2px(2), UIUtils.dip2px(1), UIUtils.dip2px(2), UIUtils.dip2px(1));
			BitmapCacheUtils.getInstance().display(imageView, HttpHelper.URL + "image?name="+pictures.get(i));
			layout.addView(imageView);
		}
		
		//
		ArrayList<Safe> safes = mData.safe;
		LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		for(int i=0;i<safes.size();i++){
			ImageView imageView = new ImageView(UIUtils.getContext());
			imageView.setLayoutParams(params);
			//imageView.setPadding(UIUtils.dip2px(2), UIUtils.dip2px(1), UIUtils.dip2px(2), UIUtils.dip2px(1));
			BitmapCacheUtils.getInstance().display(imageView, HttpHelper.URL + "image?name="+safes.get(i).safeUrl);
			layoutSafe.addView(imageView);
			View viewSafe  = UIUtils.inflate(R.layout.app_deatils_safe);
			ImageView view1 = (ImageView) viewSafe.findViewById(R.id.iv_safe1);
			TextView view2 = (TextView) viewSafe.findViewById(R.id.tv_safe1);
			viewSafe.setLayoutParams(params);
			BitmapCacheUtils.getInstance().display(view1, HttpHelper.URL + "image?name="+safes.get(i).safeDesUrl);
			view2.setText(safes.get(i).safeDes);
			layoutSafe1.addView(viewSafe);
		}
		
		relativeLayout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				System.out.println("onClick--------------------->");
				toggle();
			}
		});
		
		LayoutParams layoutsafe = (LayoutParams) layoutSafe1.getLayoutParams(); 
		layoutSafe1.measure(0, 0);
		mHeight = layoutSafe1.getMeasuredHeight();
		layoutsafe.height = 0;
		layoutSafe1.setLayoutParams(layoutsafe);
		
		FrameLayout.LayoutParams down = new android.widget.FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT);
		final MyHoriProgress pbProgress = new MyHoriProgress(UIUtils.getContext());
		pbProgress.setProgressBackgroundResource(R.drawable.progress_bg);// 进度条背景图片
		pbProgress.setProgressResource(R.drawable.progress_normal);// 进度条图片
		pbProgress.setProgressTextColor(Color.WHITE);// 进度文字颜色
		pbProgress.setProgressTextSize(UIUtils.dip2px(18));// 进度文字大小
		
		DownLoadInfo downLoadInfo = mDownLoadManager.getDownLoadInfo(copy(mData));
		if(downLoadInfo != null){
			mCurrenState = downLoadInfo.currentState;
		}else{
			mCurrenState = DownLoadManager.STATE_UNDO;
		}
		pbProgress.setStyle(mCurrenState);
		pbProgress.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if(mCurrenState == DownLoadManager.STATE_UNDO || mCurrenState == DownLoadManager.STATE_WAITING
						|| mCurrenState == DownLoadManager.STATE_FAILED || mCurrenState == DownLoadManager.STATE_PAUSE){
					mDownLoadManager.downLoad(copy(mData));
					
				}else if(mCurrenState == DownLoadManager.STATE_DOWNLOADING){
					mDownLoadManager.pause(copy(mData));
				}else{
					mDownLoadManager.install(copy(mData));
				}
			}
		});
		
		mFrameLayoutDownLoad.addView(pbProgress, down);
		
		mDownLoadManager.registerAbserve(new DownLoadAbserve() {
			@Override
			public void notifyAllAbservesStateChanged(DownLoadInfo loadInfo) {
				if(!loadInfo.id.equals(mData.id)){
					return;
				}
				mCurrenState = loadInfo.currentState;
				pbProgress.setStyle(mCurrenState);
			}
			
			@Override
			public void notifyAllAbservesProcessChanged(DownLoadInfo loadInfo) {
				if(!loadInfo.id.equals(mData.id)){
					return;
				}
				pbProgress.setProgress(loadInfo.getProgress(), true);
			}
		});
		return view;
	}
	
	@SuppressLint("NewApi")
	private void toggle(){
		ValueAnimator animator = null;
		final LayoutParams layoutParams = (LayoutParams) layoutSafe1.getLayoutParams(); 
		if(mIsDown){
			animator = ValueAnimator.ofInt(0,mHeight);
		}else{
			animator =ValueAnimator.ofInt(mHeight,0);
		}
		animator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				layoutParams.height = (Integer) arg0.getAnimatedValue();
				System.out.println("layoutParams.height" + layoutParams.height);
			    layoutSafe1.setLayoutParams(layoutParams);
			}
		});
		animator.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {
				
			}
			@Override
			public void onAnimationRepeat(Animator arg0) {

			}
			@Override
			public void onAnimationEnd(Animator arg0) {
				if(mIsDown){
					mIsDown = false;
					safePull.setImageResource(R.drawable.arrow_up);
				}else{
					mIsDown = true;
					safePull.setImageResource(R.drawable.arrow_down);
				}
			}
			@Override
			public void onAnimationCancel(Animator arg0) {
				
			}
		});
		animator.setDuration(200);
		animator.start();
	}
	
	public AppInfo copy(AppDetailsInfo appDetailsInfo){
		AppInfo info = new AppInfo();
		info.id = appDetailsInfo.id;
		info.des = appDetailsInfo.des;
		info.downloadUrl = appDetailsInfo.downloadUrl;
		info.iconUrl = appDetailsInfo.iconUrl;
		info.name = appDetailsInfo.name;
		info.packageName = appDetailsInfo.packageName;
		info.size = appDetailsInfo.size;
		info.stars = appDetailsInfo.stars;

		return info;
	}

	private ResultState onLoad() {
		HomeDetailsProctol proctol = new HomeDetailsProctol(packageName);
		mData = proctol.getData(0);
		return ResultState.STATE_SUCCESS;
	}
}
