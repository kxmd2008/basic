package org.luis.basic.util;

import java.util.ArrayList;
import java.util.List;


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
	
	public static List<String> parseStr(String str, String del){
		List<String> list = new ArrayList<String>();
		if(!isNullOrBlank(str) && !isNullOrBlank(del)){
			String[] temp = str.split(del);
			for (String string : temp) {
				list.add(string);
			}
		}
		return list;
	}
}
