package org.luis.basic.util;

import java.io.IOException;

public class BasicUtil {
	
	/**
	 * 返回WebApp的物理地址
	 */
	public static String getWebAppPath() {
		String path = "";
		try {
			// 通过Spring ApplicationContext获取资源绝对路径
			path = SpringContextAware.getApplicationContext().getResource("")
					.getFile().getAbsolutePath()
					+ "/";
		} catch (IOException e) {
			e.printStackTrace();
		}
		return path;
	}
	
}
