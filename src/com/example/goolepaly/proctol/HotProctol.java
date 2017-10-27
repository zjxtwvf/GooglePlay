package com.example.goolepaly.proctol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;

public class HotProctol extends BaseProctol<ArrayList<String>>{

	@Override
	public String getKey() {
		return "hot";
	}

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public ArrayList<String> parseData(String data) {
		ArrayList<String> strings = new ArrayList<String>();
		try {
			JSONArray jo = new JSONArray(data);
			for(int i=0;i<jo.length();i++){
				strings.add(jo.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return strings;
	}
}
