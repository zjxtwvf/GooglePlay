package com.example.goolepaly.proctol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.goolepaly.domain.AppInfo;

public class AppProctol extends BaseProctol<ArrayList<AppInfo>> {

	ArrayList<AppInfo> mListData;
	
	@Override
	public String getKey() {
		return "app";
	}

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public ArrayList<AppInfo> parseData(String data) {
		mListData = new ArrayList<AppInfo>();
		JSONArray ja = null;
		try {
			ja = new JSONArray(data);
			for(int i=0;i<ja.length();i++){
				JSONObject jo = ja.getJSONObject(i);
				AppInfo appInfo = new AppInfo();
				appInfo.des = jo.getString("des");
				appInfo.downloadUrl = jo.getString("downloadUrl");
				appInfo.iconUrl = jo.getString("iconUrl");
				appInfo.id = jo.getString("id");
				appInfo.name = jo.getString("name");
				appInfo.packageName = jo.getString("packageName");
				appInfo.size = jo.getLong("size");
				appInfo.stars = jo.getLong("stars");
				mListData.add(appInfo);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}		
		
		return mListData;
	}
}
