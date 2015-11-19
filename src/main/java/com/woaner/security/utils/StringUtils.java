package com.woaner.security.utils;

public class StringUtils {
	//验证字符串为空
	public static boolean verify_not_empty(String str){
		return !(str==null|| str.isEmpty());
	}
}
