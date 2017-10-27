package com.example.goolepaly.proctol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.goolepaly.domain.AppInfo;

public class HomeProctol extends BaseProctol<ArrayList<AppInfo>> {

	private ArrayList<String> pictures;

	@Override
	public String getKey() {
		return "home";
	}

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public ArrayList<AppInfo> parseData(String data) {
		try {
			JSONObject jo = new JSONObject(data);
			JSONArray ja = jo.getJSONArray("list");
			ArrayList<AppInfo> parseResult = new ArrayList<AppInfo>();
			AppInfo appInfo = null;
			
			for(int i=0;i<ja.length();i++){
				JSONObject jo1 = ja.getJSONObject(i);
				appInfo = new AppInfo();
				appInfo.des = jo1.getString("des");
				appInfo.downloadUrl = jo1.getString("downloadUrl");
				appInfo.iconUrl = jo1.getString("iconUrl");
				appInfo.id = jo1.getString("id");
				appInfo.name = jo1.getString("name");
				appInfo.packageName = jo1.getString("packageName");
				appInfo.size = jo1.getInt("size");
				appInfo.stars = (float)jo1.getDouble("stars");

				parseResult.add(appInfo);
			}
			//»ñÈ¡url
			pictures = new ArrayList<String>();
			ja = jo.getJSONArray("picture");
			for(int i=0;i<ja.length();i++){
				System.out.println("ja.getString(i)"+ja.getString(i));
				pictures.add(ja.getString(i));
			}
			
			return parseResult;
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public ArrayList<String> getPictures(){
		return pictures;
	}
}
