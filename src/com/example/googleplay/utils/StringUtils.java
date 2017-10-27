package com.example.googleplay.utils;


public class StringUtils {
	/** 判断字符串是否有值，如果为null或�?�是空字符串或�?�只有空格或者为"null"字符串，则返回true，否则则返回false */
	public static boolean isEmpty(String value) {
		if (value != null && !"".equalsIgnoreCase(value.trim())
				&& !"null".equalsIgnoreCase(value.trim())) {
			return false;
		} else {
			return true;
		}
	}
}
