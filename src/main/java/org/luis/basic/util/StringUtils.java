package org.luis.basic.util;


public class StringUtils {
	
	public static boolean isNullOrBlank(String str){
		if(str == null){
			return true;
		}
		if("".equals(str.trim())){
			return true;
		}
		return false;
	}
	
	public static String[] split(String src, String del) {
		return org.springframework.util.StringUtils.split(src, del);
	}
	
	
}
