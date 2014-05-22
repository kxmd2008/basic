package org.luis.basic.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;


public class SpringContextAware implements ApplicationContextAware {

	private static ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		SpringContextAware.applicationContext = applicationContext;
	}

	
	public static Object getBean(String beanId) {
		return applicationContext.getBean(beanId);
	}
	
	/**
	 * @since 2.0.1.0
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(Class<T> clazz) {
		return applicationContext.getBean(clazz);
	}
	
	/**
	 * @since 2.0.1.0
	 * @param clazz
	 * @return
	 */
	public static <T> T getBean(String beanName, Class<T> clazz) {
		return applicationContext.getBean(beanName, clazz);
	}

}
