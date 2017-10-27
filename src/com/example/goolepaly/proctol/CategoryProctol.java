package com.example.goolepaly.proctol;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.goolepaly.domain.CatogoryInfo;

public class CategoryProctol extends BaseProctol<ArrayList<CatogoryInfo>>{

	@Override
	public String getKey() {
		return "category";
	}

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public ArrayList<CatogoryInfo> parseData(String data) {
		ArrayList<CatogoryInfo> catogoryInfos = new ArrayList<CatogoryInfo>();
		try {
			JSONArray ja = new JSONArray(data);
			for(int i=0;i<ja.length();i++){
		        JSONObject jo = ja.getJSONObject(i);
		        if(jo.has("title")){
			        CatogoryInfo catogoryInfo = new CatogoryInfo();
			        catogoryInfo.title = jo.getString("title");
			        catogoryInfo.isTtilte = true;
			        catogoryInfos.add(catogoryInfo);
		        }

		        if(jo.has("infos")){
			        JSONArray ja1 = jo.getJSONArray("infos");
			        for(int j=0;j<ja1.length();j++){
			        	JSONObject jo1 = ja1.getJSONObject(j);
			        	CatogoryInfo info = new CatogoryInfo();
			        	info.isTtilte = false;
			        	info.name1 = jo1.getString("name1");
			        	info.name2 = jo1.getString("name2");
			        	info.name3 = jo1.getString("name3");
			        	info.url1 = jo1.getString("url1");
			        	info.url2 = jo1.getString("url2");
			        	info.url3 = jo1.getString("url3");
			        	catogoryInfos.add(info);
			        }
		        }
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return catogoryInfos;
	}
}
