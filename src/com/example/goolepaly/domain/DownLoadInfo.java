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

	public long currentPos;// ��ǰ����λ��
	public int currentState;// ��ǰ����״̬
	public String path;// ���ص������ļ���·��

	public static final String GOOGLE_MARKET = "GOOGLE_MARKET";// sdcard��Ŀ¼�ļ�������
	public static final String DONWLOAD = "download";// ���ļ�������, ������ص��ļ�
	
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
