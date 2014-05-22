package org.luis.basic.util;


public class SpringContextFactory {
	
	public static Object getSpringBean(String beanId) {
		try {
			return SpringContextAware.getBean(beanId);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static <T> T getSpringBean(Class<T> clazz) {
		try {
			return SpringContextAware.getBean(clazz);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static <T> T getSpringBean(String beanName, Class<T> clazz) {
		try {
			return SpringContextAware.getBean(beanName, clazz);
		} catch (Exception e) {
			return null;
		}
	}


}
