package com.example.goolepaly.domain;

import java.io.File;

import com.example.googleplay.manager.DownLoadManager;

import android.os.Environment;

public class DownLoadInfo {
    
	public String id;
	public String name;
	public String downloadUrl;
	public long size;
	public String packageName;

	public long currentPos;// 当前下载位置
	public int currentState;// 当前下载状态
	public String path;// 下载到本地文件的路径

	public static final String GOOGLE_MARKET = "GOOGLE_MARKET";// sdcard根目录文件夹名称
	public static final String DONWLOAD = "download";// 子文件夹名称, 存放下载的文件
	
	public static DownLoadInfo copy(AppInfo appInfo){
		DownLoadInfo downLoadInfo = new DownLoadInfo();
		downLoadInfo.currentPos = 0;
		downLoadInfo.id = appInfo.id;
		downLoadInfo.currentState = DownLoadManager.STATE_UNDO;
		downLoadInfo.downloadUrl = appInfo.downloadUrl;
		downLoadInfo.name = appInfo.name;
		downLoadInfo.packageName = appInfo.packageName;
		downLoadInfo.size = appInfo.size;
		downLoadInfo.path = downLoadInfo.getDownLoadPath();
		
		return downLoadInfo;
	}
	
	public float getProgress(){
		if(currentPos == 0){
			return 0;
		}else{
			return currentPos/(float)size;
		}
	}
	
	public String getDownLoadPath(){
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(Environment.getExternalStorageDirectory().getAbsolutePath());	
		stringBuffer.append(File.separator);
		stringBuffer.append(GOOGLE_MARKET);
		stringBuffer.append(File.separator);
		stringBuffer.append(DONWLOAD);
		
		if(createDir(stringBuffer.toString())){
			return stringBuffer.toString() + File.separator + name +".apk";
		}
		return null;
	}
	
	public Boolean createDir(String dir){
		File file = new File(dir);
		
		if(!file.exists() || !file.isDirectory()){
			return file.mkdirs();
		}
		
		return true;
	}
}
