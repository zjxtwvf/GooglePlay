package com.example.goolepaly.proctol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.goolepaly.domain.AppDetailsInfo;
import com.example.goolepaly.domain.AppDetailsInfo.Safe;


public class HomeDetailsProctol extends BaseProctol<AppDetailsInfo>{
	private String packageName;
	public HomeDetailsProctol(String packageName){
		this.packageName = packageName;
	}
	@Override
	public String getKey() {
		return "detail";
	}
	@Override
	public String getParams() {
		return "&packageName=" + packageName;
	}
	@Override
	public AppDetailsInfo parseData(String data) {
		AppDetailsInfo appDetailsInfo = new AppDetailsInfo();
        try {
			JSONObject jo = new JSONObject(data);
			appDetailsInfo.id = jo.getString("id");
			appDetailsInfo.author = jo.getString("author");
			appDetailsInfo.date = jo.getString("date");
			appDetailsInfo.des = jo.getString("des");
			appDetailsInfo.downloadNum = jo.getString("downloadNum");
			appDetailsInfo.downloadUrl = jo.getString("downloadUrl");
			appDetailsInfo.iconUrl = jo.getString("iconUrl");
			appDetailsInfo.name = jo.getString("name");
			appDetailsInfo.packageName = jo.getString("packageName");
			appDetailsInfo.size = jo.getLong("size");
			appDetailsInfo.version = jo.getString("version");
			appDetailsInfo.stars = (float)jo.getDouble("stars");
			
			JSONArray ja = jo.getJSONArray("screen");
			ArrayList<String> screens = new ArrayList<String>();
			for(int i=0;i<ja.length();i++){
				screens.add(ja.getString(i));
			}
			appDetailsInfo.screen = screens;
			
	        ja = jo.getJSONArray("safe");
	        ArrayList<Safe> safes = new ArrayList<Safe>();
			for(int i=0;i<ja.length();i++){
				JSONObject jo1 = ja.getJSONObject(i);
				Safe safe = new Safe();
				safe.safeDes = jo1.getString("safeDes");
				safe.safeDesColor = jo1.getInt("safeDesColor");
				safe.safeDesUrl = jo1.getString("safeDesUrl");
				safe.safeUrl = jo1.getString("safeUrl");
				safes.add(safe);
			}
			
			appDetailsInfo.safe = safes;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
        
		return appDetailsInfo;
	}

}
