package com.example.goolepaly.domain;

import java.util.ArrayList;

public class AppDetailsInfo {
    public String id;
    public String name;
    public String packageName;
    public String iconUrl; 
    public float stars;
    public String downloadNum;
    public String version;
    public String date;
    public long size;
    public String downloadUrl;
    public String des;
    public String author;
    public ArrayList<String> screen;
    public ArrayList<Safe> safe;
    public static class Safe{
    	public String safeUrl;
    	public String safeDesUrl;
    	public String safeDes;
    	public int safeDesColor;
    }
}