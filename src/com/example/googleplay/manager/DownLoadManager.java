package com.example.googleplay.manager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;
import com.example.googleplay.manager.DownLoadManager;
import com.example.googleplay.utils.UIUtils;
import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.http.HttpHelper.HttpResult;
import com.example.goolepaly.domain.AppInfo;
import com.example.goolepaly.domain.DownLoadInfo;

public class DownLoadManager {

	public static final int STATE_UNDO = 0;
	public static final int STATE_DOWNLOADING = 1;
	public static final int STATE_WAITING = 2;
	public static final int STATE_PAUSE = 3;
	public static final int STATE_FAILED = 4;
	public static final int STATE_SUCESS = 5;
	
	private static DownLoadManager manager;

	//
	ArrayList<DownLoadAbserve> mAbservers = new ArrayList<DownLoadManager.DownLoadAbserve>();
	HashMap<String, DownLoadTask> mDownLoadTasks = new HashMap<String, DownLoadManager.DownLoadTask>();
	HashMap<String, DownLoadInfo> mDownLoadInfos = new HashMap<String, DownLoadInfo>();
	
	private DownLoadManager(){
	}
	
	public static synchronized DownLoadManager getInstence(){
		if(manager == null){
			manager = new DownLoadManager();
		}
		return manager;
	}

	public void downLoad(AppInfo appInfo) {
		DownLoadInfo loadInfo;
		if (null != mDownLoadInfos.get(appInfo.id)) {
			loadInfo = mDownLoadInfos.get(appInfo.id);
		} else {
			loadInfo = DownLoadInfo.copy(appInfo);
			mDownLoadInfos.put(loadInfo.id, loadInfo);
		}
		if (mDownLoadTasks.get(loadInfo.id) != null) {
			ThreadManager.getThreadPool().execute(
					mDownLoadTasks.get(loadInfo.id));
		} else {
			mDownLoadTasks.put(loadInfo.id, new DownLoadTask(loadInfo));
			ThreadManager.getThreadPool().execute(
					mDownLoadTasks.get(loadInfo.id));
		}
	}

	public void pause(AppInfo appInfo) {
		DownLoadInfo loadInfo;
		if (null != mDownLoadInfos.get(appInfo.id)) {
			loadInfo = mDownLoadInfos.get(appInfo.id);
			if (loadInfo.currentState != DownLoadManager.STATE_DOWNLOADING) {
				return;
			} else {
				loadInfo.currentState = DownLoadManager.STATE_PAUSE;
				notifyAllAbserStateChanged(loadInfo);
				ThreadManager.getThreadPool().cancel(
						mDownLoadTasks.get(loadInfo.id));
			}
		} else {
			return;
		}
	}

	public void install(AppInfo appInfo) {
		DownLoadInfo loadInfo;
		if (null != mDownLoadInfos.get(appInfo.id)) {
			loadInfo = mDownLoadInfos.get(appInfo.id);
			if (loadInfo.currentState != DownLoadManager.STATE_SUCESS) {
				Toast.makeText(UIUtils.getContext(), "error",
						Toast.LENGTH_SHORT).show();
				return;
			} else {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.setDataAndType(Uri.parse("file://" + loadInfo.path),
						"application/vnd.android.package-archive");
				UIUtils.getContext().startActivity(intent);
			}
		} else {
			Toast.makeText(UIUtils.getContext(), "error", Toast.LENGTH_SHORT)
					.show();
			return;
		}
	}

	public void notifyAllAbserStateChanged(DownLoadInfo loadInfo) {
		for (int i = 0; i < mAbservers.size(); i++) {
			mAbservers.get(i).notifyAllAbservesStateChanged(loadInfo);
		}
	}

	public void notifyAllAbserveProcessChanged(DownLoadInfo loadInfo) {
		for (int i = 0; i < mAbservers.size(); i++) {
			mAbservers.get(i).notifyAllAbservesProcessChanged(loadInfo);
		}
	}

	public void registerAbserve(DownLoadAbserve abserve) {
		if (abserve != null && (!mAbservers.contains(abserve))) {
			mAbservers.add(abserve);
		}
	}

	public void unRegisterAbserve(DownLoadAbserve abserve) {
		if (abserve != null && (!mAbservers.contains(abserve))) {
			mAbservers.remove(abserve);
		}
	}

	class DownLoadTask implements Runnable {
		public DownLoadInfo loadInfo;
		DownLoadTask(DownLoadInfo loadInfo) {
			this.loadInfo = loadInfo;
		}
		@Override
		public void run() {
			HttpResult httpResult = null;
			loadInfo.currentState = DownLoadManager.STATE_DOWNLOADING;
			notifyAllAbserStateChanged(loadInfo);
			File file = new File(loadInfo.path);

			if (!file.exists() || file.length() != loadInfo.currentPos) {
				file.delete();
				loadInfo.currentPos = 0;
				httpResult = HttpHelper.download(HttpHelper.URL
						+ "download?name=" + loadInfo.downloadUrl);
			} else {
				httpResult = HttpHelper.download(HttpHelper.URL
						+ "download?name=" + loadInfo.downloadUrl + "&range="
						+ file.length());
			}

			if (httpResult != null && httpResult.getInputStream() != null) {
				byte[] buffer = new byte[1024];
				InputStream inputStream = httpResult.getInputStream();
				FileOutputStream fileOutputStream;
				try {
					fileOutputStream = new FileOutputStream(new File(
							loadInfo.path), true);
					int lenght = 0;
					while (((lenght = inputStream.read(buffer)) != -1)
							&& (loadInfo.currentState == DownLoadManager.STATE_DOWNLOADING)) {
						fileOutputStream.write(buffer, 0, lenght);
						loadInfo.currentPos += lenght;
						notifyAllAbserveProcessChanged(loadInfo);
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				if (file.length() == loadInfo.size) {
					loadInfo.currentState = DownLoadManager.STATE_SUCESS;
				} else if (loadInfo.currentState == DownLoadManager.STATE_PAUSE) {
				} else {
					file.delete();
					loadInfo.currentState = DownLoadManager.STATE_FAILED;
				}
				notifyAllAbserveProcessChanged(loadInfo);
				notifyAllAbserStateChanged(loadInfo);

			} else {
				file.delete();
				loadInfo.currentPos = 0;
				loadInfo.currentState = DownLoadManager.STATE_FAILED;
				notifyAllAbserStateChanged(loadInfo);
			}
		}
	}
	
	public DownLoadInfo getDownLoadInfo(AppInfo appInfo){
		DownLoadInfo downLoadInfo = mDownLoadInfos.get(appInfo.id);
		if(downLoadInfo != null){
			return downLoadInfo;
		}
		return null;
	}

	public interface DownLoadAbserve {
		public void notifyAllAbservesStateChanged(DownLoadInfo loadInfo);
		public void notifyAllAbservesProcessChanged(DownLoadInfo loadInfo);
	}
}
