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
		if (src == null)
			return null;

		if (del == null) {
			String[] res = new String[1];
			res[0] = src;
			return (res);
		}
		if (src.equals("")) {
			String[] res = new String[1];
			res[0] = src;
			return (res);
		}

		String[] res = new String[src.length()];
		String src1 = new String(src);
		int i = 0, j = 0;

		for (; !src1.equals("");) {
			i = src1.indexOf(del);
			if (i == -1)
				break;
			res[j++] = src1.substring(0, i);
			src1 = src1.substring(i + del.length());
		}

		if (!src1.equals(""))
			res[j] = src1;

		// shrink the string array
		String[] ret = new String[j + 1];
		for (i = 0; i <= j; i++) {
			ret[i] = res[i];
			res[i] = null;
		}

		res = null;
		return ret;
	}
}
