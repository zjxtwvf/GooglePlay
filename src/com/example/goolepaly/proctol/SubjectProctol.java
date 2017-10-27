package com.example.goolepaly.proctol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.goolepaly.domain.SubjectInfo;

public class SubjectProctol extends BaseProctol<ArrayList<SubjectInfo>>{

	@Override
	public String getKey() {
		return "subject";
	}

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public ArrayList<SubjectInfo> parseData(String data) {
		ArrayList<SubjectInfo> result = new ArrayList<SubjectInfo>();
		try {
			JSONArray ja = new JSONArray(data);
			for(int i=0;i<ja.length();i++){
				JSONObject jo = ja.getJSONObject(i);
				SubjectInfo subjectInfo = new SubjectInfo();
				subjectInfo.des = jo.getString("des");
				subjectInfo.url = jo.getString("url");
				result.add(subjectInfo);
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return result;
	}

}
