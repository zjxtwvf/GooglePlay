package com.example.goolepaly.proctol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import android.text.TextUtils;

import com.example.googleplay.http.HttpHelper;
import com.example.googleplay.http.HttpHelper.HttpResult;
import com.example.googleplay.utils.UIUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

public abstract class BaseProctol<T> {
	
	public static final String URL = "http://127.0.0.1:8090/";
	
	public T getData(int index) {
		 String result = null;
         if(getCache(index) == null){
        	 result = getDataFromNet(index);
         }
         
         if(null != result){
             T data = parseData(result);
             return data;
         }
         
         return null;
	}

	public String getDataFromNet(int index) {
		String url = URL+ getKey()
				+ "?index=" + index + getParams();
		HttpResult result = HttpHelper.get(url);		
		if(null != result){
			String resultString = result.getString();
			if(!TextUtils.isEmpty(resultString)){
				setCache(resultString, index);
			}
			return resultString;
		}
		return null;
		
		
		/*
		final String result[] = {null};
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
			@Override
			public void onFailure(HttpException error, String msg) {
                System.out.println("onFailure ------------ >");
			}
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo) {
				result[0] = responseInfo.result;
			}
		});
		
		if(result != null && !TextUtils.isEmpty(result[0])){
			setCache(result[0], index);
		}

		return result[0];
		*/
	}

	public String getCache(int index) {
		
		String url = getKey() + "?index=" + index
				+ getParams();
		File file = new File(UIUtils.getContext().getCacheDir(),url);
		BufferedReader bufferedReader = null;
		if(file.exists()){
			try {
				bufferedReader = new BufferedReader(new FileReader(file));
				String deadLine = bufferedReader.readLine();
				StringBuffer sb = new StringBuffer();
				String line = null;
				if(Long.getLong(deadLine) < java.lang.System.currentTimeMillis()){
					while((line = bufferedReader.readLine())!= null){
						sb.append(line);
					}
				}else{
					return null;
				}
				
				return sb.toString();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public void setCache(String result,int index) {
		String url = getKey() + "?index=" + index
				+ getParams();
        if(result != null && TextUtils.isEmpty(result)){
        	File newFile = new File(UIUtils.getContext().getCacheDir(),url);
        	FileWriter fileWriter = null;
        	try {
				fileWriter = new FileWriter(newFile);
				long deadLine = java.lang.System.currentTimeMillis() + 30 * 60 * 1000;;
				fileWriter.write(deadLine+"\n");
                fileWriter.write(result);	
                fileWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					fileWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
        }
	}

	public abstract String getKey();

	public abstract String getParams();
	
	public abstract T parseData(String data);
}
