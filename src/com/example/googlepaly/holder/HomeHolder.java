package com.example.googlepaly.holder;

import com.example.googlepaly.view.MyProgressArc;
import com.example.googleplay.R;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.manager.DownLoadManager;
import com.example.googleplay.manager.DownLoadManager.DownLoadAbserve;
import com.example.googleplay.utils.BitmapCacheUtils;
import com.example.googleplay.utils.UIUtils;
import com.example.goolepaly.domain.AppInfo;
import com.example.goolepaly.domain.DownLoadInfo;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class HomeHolder extends BaseHolder<AppInfo> implements OnClickListener{
	private TextView textAppName;
	private TextView appSize;
	private TextView appDec;
	private ImageView appImage;
	private RatingBar mRating;
	private FrameLayout mLayout;
	private DownLoadManager manager;
	private int mCurrentState;
	private TextView mDownLoadText;
	private MyProgressArc progressArc;
	private float mProgress;

	public HomeHolder() {
		super();
	}
	@Override
	public View initView() {
		View mItemView = UIUtils.inflate(R.layout.list_item_view);
		textAppName = (TextView) mItemView.findViewById(R.id.tv_home_appname);
		appSize = (TextView) mItemView.findViewById(R.id.tv_home_app_size);
		appDec = (TextView) mItemView.findViewById(R.id.tv_home_app_dec);
		appImage = (ImageView) mItemView.findViewById(R.id.iv_home_app);
		mRating = (RatingBar) mItemView.findViewById(R.id.rb_star);
		mLayout = (FrameLayout) mItemView.findViewById(R.id.fl_progress);
		mDownLoadText = (TextView) mItemView.findViewById(R.id.tv_download);
		//progressArc = new ProgressArc(UIUtils.getContext());
		progressArc = new MyProgressArc(UIUtils.getContext());
		progressArc.setArcDiameter(UIUtils.dip2px(26));
		progressArc.setProgressColor(Color.BLUE);
		LayoutParams layoutParams = new LayoutParams(UIUtils.dip2px(27),UIUtils.dip2px(27));
		mLayout.addView(progressArc, layoutParams);
		mLayout.setOnClickListener(this);
		manager = DownLoadManager.getInstence();
		manager.registerAbserve(new DownLoadAbserve() {
			@Override
			public void notifyAllAbservesStateChanged(DownLoadInfo loadInfo) {
				//if(loadInfo.id.equals(data.id)){
					refreshUIOnMainThread(loadInfo);
				//}
			}
			@Override
			public void notifyAllAbservesProcessChanged(DownLoadInfo loadInfo) {
				//if(loadInfo.id.equals(data.id)){
					refreshUIOnMainThread(loadInfo);
				//}
			}
		});
		return mItemView;
	}

	@Override
	public void refreshView(){
		textAppName.setText(data.name);
		appSize.setText(Long.toString(data.size/1024/1024)+"MB");
		appDec.setText(data.des);
		mRating.setRating(data.stars);
		
		//mBitmapHelper.display(appImage, HttpHelper.URL + "image?name="
			//	+ data.iconUrl);
		
		BitmapCacheUtils.getInstance().display(appImage, HttpHelper.URL + "image?name="
				+ data.iconUrl);
		
		if(manager.getDownLoadInfo(data) != null){
			mCurrentState = manager.getDownLoadInfo(data).currentState;
			mProgress = manager.getDownLoadInfo(data).getProgress();
		}else{
			mCurrentState = DownLoadManager.STATE_UNDO;
			mProgress = 0;
		}
		
		refreshUI(mCurrentState, mProgress, data.id);
	}
	
	private void refreshUI(int state, float progress, String id) {
		// 由于listview重用机制, 要确保刷新之前, 确实是同一个应用
		if (!data.id.equals(id)) {
			return;
		}

		mCurrentState = state;
		mProgress = progress;
		switch (state) {
		case DownLoadManager.STATE_UNDO:
			// 自定义进度条背景
			progressArc.setBackgroundResource(R.drawable.ic_download);
			// 没有进度
			progressArc.setStyle(MyProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			mDownLoadText.setText("下载");
			break;
		case DownLoadManager.STATE_WAITING:
			progressArc.setBackgroundResource(R.drawable.ic_download);
			// 等待模式
			progressArc.setStyle(MyProgressArc.PROGRESS_STYLE_WAITING);
			mDownLoadText.setText("等待");
			break;
		case DownLoadManager.STATE_DOWNLOADING:
			progressArc.setBackgroundResource(R.drawable.ic_pause);
			// 下载中模式
			progressArc.setStyle(MyProgressArc.PROGRESS_STYLE_DOWNLOADING);
			progressArc.setProgress(progress, true);
			mDownLoadText.setText((int) (progress * 100) + "%");
			break;
		case DownLoadManager.STATE_PAUSE:
			progressArc.setBackgroundResource(R.drawable.ic_resume);
			progressArc.setStyle(MyProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			break;
		case DownLoadManager.STATE_FAILED:
			progressArc.setBackgroundResource(R.drawable.ic_redownload);
			progressArc.setStyle(MyProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			mDownLoadText.setText("下载失败");
			break;
		case DownLoadManager.STATE_SUCESS:
			progressArc.setBackgroundResource(R.drawable.ic_install);
			progressArc.setStyle(MyProgressArc.PROGRESS_STYLE_NO_PROGRESS);
			mDownLoadText.setText("安装");
			break;

		default:
			break;
		}
	}

	// 主线程更新ui 3-4
	private void refreshUIOnMainThread(final DownLoadInfo info) {
		// 判断下载对象是否是当前应用
		AppInfo appInfo = data;
		if (appInfo.id.equals(info.id)) {
			UIUtils.runOnUIThread(new Runnable() {
				@Override
				public void run() {
					refreshUI(info.currentState, info.getProgress(), info.id);
				}
			});
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fl_progress:
			// 根据当前状态来决定下一步操作
			if (mCurrentState == DownLoadManager.STATE_UNDO
					|| mCurrentState == DownLoadManager.STATE_FAILED
					|| mCurrentState == DownLoadManager.STATE_PAUSE) {
				manager.downLoad(data);// 开始下载
			} else if (mCurrentState == DownLoadManager.STATE_DOWNLOADING
					|| mCurrentState == DownLoadManager.STATE_WAITING) {
				manager.pause(data);// 暂停下载
			} else if (mCurrentState == DownLoadManager.STATE_SUCESS) {
				manager.install(data);// 开始安装
			}
			break;
		default:
			break;
		}
	}
}
