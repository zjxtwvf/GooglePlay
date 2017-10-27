package com.example.googleplay.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Utils {
	
	/**
	 * @param path
	 *    锟侥硷拷锟斤拷路锟斤拷
	 * @return
	 *    锟侥硷拷锟斤拷MD5值
	 */
	public static String getFileMD5(String path){
		StringBuilder mess = new StringBuilder();
		try {
			FileInputStream fis = new FileInputStream(new File(path));
			//锟斤拷取MD5锟斤拷锟斤拷锟斤拷
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] buffer = new byte[10240];
			int len = fis.read(buffer);
			while (len != -1) {
				md.update(buffer, 0, len);
				//锟斤拷锟斤拷锟饺�
				len = fis.read(buffer);
			}
			byte[] digest = md.digest();
			for (byte b : digest){
				//锟斤拷每锟斤拷锟街斤拷转锟斤拷16锟斤拷锟斤拷锟斤拷  
				int d = b & 0xff;// 0x000000ff
				String hexString = Integer.toHexString(d);
				if (hexString.length() == 1) {//锟街节的革拷4位为0
					hexString = "0" + hexString;
				}
				hexString = hexString.toUpperCase();
				mess.append(hexString);//锟斤拷每锟斤拷锟街节讹拷应锟斤拷2位十锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷拼锟斤拷一锟斤拷
				
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mess + "";
	}
	public static String md5(String str){
		StringBuilder mess = new StringBuilder();
		try {
			//锟斤拷取MD5锟斤拷锟斤拷锟斤拷
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] bytes = str.getBytes();
			byte[] digest = md.digest(bytes);
			
			for (byte b : digest){
				//锟斤拷每锟斤拷锟街斤拷转锟斤拷16锟斤拷锟斤拷锟斤拷  
				int d = b & 0xff;// 0x000000ff
				String hexString = Integer.toHexString(d);
				if (hexString.length() == 1) {//锟街节的革拷4位为0
					hexString = "0" + hexString;
				}
				mess.append(hexString);//锟斤拷每锟斤拷锟街节讹拷应锟斤拷2位十锟斤拷锟斤拷锟斤拷锟斤拷锟街凤拷拼锟斤拷一锟斤拷
				
			}
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mess + "";
	}
}
