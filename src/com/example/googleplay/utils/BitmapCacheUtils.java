package com.example.googleplay.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import com.example.googleplay.manager.ThreadManager;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

public class BitmapCacheUtils {
	
	private LruCache<String, Bitmap> mCache;
	private HashMap<ImageView, String> mHashMap = new HashMap<ImageView, String>();
	
	private static BitmapCacheUtils mCacheUtils;
    	
	private BitmapCacheUtils(){
		long maxSize = Runtime.getRuntime().freeMemory();
		mCache = new LruCache<String, Bitmap>((int)(maxSize/2)){
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getRowBytes()*value.getHeight();
			}
		};
	}
	
	public static BitmapCacheUtils getInstance(){
		if(mCacheUtils != null){
			return mCacheUtils;
		}
		mCacheUtils = new BitmapCacheUtils();
		
		return mCacheUtils;
	}

	public void display(ImageView imageView,String url){
		Bitmap bitmap = null;
		//
		mHashMap.put(imageView, url);
		//从内存获取
		bitmap = getFromMap(url);
		if(bitmap != null){
			imageView.setImageBitmap(bitmap);
			//System.out.println("从map获取。。。。。。。。。。。。");
		}
		//从File获取
		if((null == bitmap) && ((bitmap = getFromFile(url)) != null)){
			//System.out.println("从file获取。。。。。。。。。。。。");
			setBitmapToMap(bitmap,url);
			imageView.setImageBitmap(bitmap);
		}
		//从网络获取
		if((null == bitmap)){
			getFromNet(imageView,url);
			//System.out.println("从net获取。。。。。。。。。。。。");
		}
	}

	private void setBitmapToFile(Bitmap bitmap, String url) {
		String cacheDir = UIUtils.getContext().getCacheDir().getAbsolutePath();
		File file = new File(cacheDir + Md5Utils.md5(url));

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
		}
		
	}

	private void setBitmapToMap(Bitmap bitmap, String url) {
		mCache.put(Md5Utils.md5(url), bitmap);
	}

	private void getFromNet(final ImageView imageView,final String url) {
		ThreadManager.getThreadPool().execute(new Runnable() {
			Bitmap bitmap = null;
			@Override
			public void run() {
				try {
					URL url2 = new URL(url);
					HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
					connection.setConnectTimeout(5000);
					connection.setRequestMethod("GET");
					if(connection.getResponseCode() != 200){
						return;
					}
					InputStream inputStream = connection.getInputStream();
					if(null != inputStream){
						bitmap = BitmapFactory.decodeStream(inputStream);
						setBitmapToMap(bitmap,url);
						setBitmapToFile(bitmap,url);
						UIUtils.runOnUIThread(new Runnable() {
							@Override
							public void run() {
								if(url.equals(mHashMap.get(imageView))){
									imageView.setImageBitmap(bitmap);
								}
							}
						});
					}
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Bitmap getFromFile(String url) {
		Bitmap bitmap = null;
		String cacheDir = UIUtils.getContext().getCacheDir().getAbsolutePath();
		File file = new File(cacheDir + Md5Utils.md5(url));
		
		if(file.exists()){
			bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
		}
		
		if(null != bitmap){
			return bitmap;
		}
		return null;
	}

	private Bitmap getFromMap(String url) {
		return mCache.get(Md5Utils.md5(url));
	}
	
	
}
