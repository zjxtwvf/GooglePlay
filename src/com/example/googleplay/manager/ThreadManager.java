package com.example.googleplay.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;

public class ThreadManager {
	private static ThreadPool threadPool;
	
	public static synchronized ThreadPool getThreadPool(){
		if(null == threadPool){
			int cpuCount = Runtime.getRuntime().availableProcessors();
			int coreThreadCnt = 2*cpuCount;
			threadPool = new ThreadPool(coreThreadCnt, coreThreadCnt, 1);
		}
		return threadPool;
	}
	
	public static class ThreadPool {
		public int coreThreadCnt;
		public int maxThreadCnt;
		public int keepAlive;
		public ThreadPoolExecutor executor;

		public ThreadPool(int coreThreadCnt, int maxThreadCnt, int keepAlive) {
			this.coreThreadCnt = coreThreadCnt;
			this.keepAlive = keepAlive;
			this.maxThreadCnt = maxThreadCnt;
		}

		@SuppressLint("NewApi")
		public void execute(Runnable r){
			if(null == executor){
				executor = new ThreadPoolExecutor(coreThreadCnt, maxThreadCnt, keepAlive,TimeUnit.SECONDS, 
						new LinkedBlockingDeque<Runnable>(),Executors.defaultThreadFactory(),new AbortPolicy());
			}
			executor.execute(r);
		}

		public void cancel(Runnable r) {
            if(null != executor){
                executor.getQueue().remove(r);
            }
		}
	}
}
