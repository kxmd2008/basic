package org.luis.basic.common;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;
import org.luis.basic.util.BasicUtil;
import org.luis.basic.util.StringUtils;

/**
 * 加载config.properties文件所对应的对象
 * 
 */
public class ProcessContext implements java.io.Serializable {

	public static String PROPKEY_SCHEDULE_JOB = "schedule.job";

	/**
	 * 
	 */
	private static final long serialVersionUID = -7313137799588294434L;

	protected static Logger logger = Logger.getLogger(ProcessContext.class);

	/**
	 * 定义应用的根目录bin/lib/web/conf等的上一级目录
	 */
	private String rootPath = null;

	/**
	 * 获得根目录地址
	 * 
	 * @return
	 */
	public String getRootPath() {
		if (rootPath == null) {
			try {
				rootPath = BasicUtil.getWebAppPath();
			} catch (Exception e) {
				
			}
		}
		return rootPath;
	}

	/**
	 * 对应缺省的ngbf-spring.properties文件
	 */
	public Configuration properties;

	/**
	 * 对应缺省的ngbf-spring-ext.properties，优先级高于properties
	 */
	public Configuration extProperties;

	/**
	 * 注册所有的Configuration对象，即系统的Properties文件对应的Configuration对象
	 * 
	 */
	public Map<String, Configuration> configurations = new HashMap<String, Configuration>();

	/**
	 * 启动参数Map
	 * 
	 */
	public Map<String, String> argsMap = new HashMap<String, String>();

	/**
	 * 注册Configuration对象
	 * 
	 */
	public void register(String key, Configuration config) {
		configurations.put(key, config);
	}

	/**
	 * 获取Configuration对象
	 * 
	 */
	public Configuration getConfiguration(String key) {
		return configurations.get(key);
	}

	/**
	 * 构造函数
	 * 
	 * @param properties
	 * @param extProperties
	 */
	public void init(Configuration properties, Configuration extProperties) {
		this.properties = properties;
		this.extProperties = extProperties;
		Iterator<String> it = extProperties.getKeys();
		while (it.hasNext()) {
			String key = it.next();
			if (properties.containsKey(key)) {
				properties.clearProperty(key);
			}
			properties.addProperty(key, extProperties.getProperty(key));
		}
		mergeArgsMap();
	}

	/**
	 * 动态参数合并到extProperties
	 */
	public void mergeArgsMap() {
		if (!argsMap.isEmpty()) {
			Iterator<Entry<String, String>> it = argsMap.entrySet().iterator();
			while (it.hasNext()) {
				Entry<String, String> e = it.next();
				extProperties.setProperty(e.getKey(), e.getValue());
			}
		}
	}

	/**
	 * 主从模式，判定该进程服务是否是主进程服务，是否是是通过ProcessWatcher进行设置的
	 */
	public boolean master = false;

	public boolean isMaster() {
		return master;
	}

	public synchronized void setMaster(boolean master) {
		this.master = master;
	}

	/**
	 * 获得字符串值
	 * 
	 */
	public String getStrPropValue(String key, String defaultValue) {
		String value = null;
		try {
			value = extProperties.getString(key);
			if (value == null) {
				value = properties.getString(key, defaultValue);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return value;
	}

	/**
	 * 获得key对应的多个值,Properties可以存在多个相同的值
	 * 
	 * @param key
	 * @return
	 */
	public String[] getStrPropValues(String key) {
		String[] values = null;
		try {
			List<Object> list = extProperties.getList(key);
			if (list == null || list.size() == 0) {
				list = properties.getList(key);
			}

			if (list != null && list.size() > 0) {
				// 翻译成List<String>,祛除""
				StringBuffer sb = new StringBuffer();
				for (Object object : list) {
					String v = object.toString().trim();
					if (!StringUtils.isNullOrBlank(v)) {
						sb.append(v + ",");
					}
				}
				values = StringUtils.stringToArray(sb.toString());
			}
		} catch (Exception e) {
			values = new String[0];
		}
		return values;
	}
	
	/**
	 * 获得整型数值
	 * 
	 */
	public int getIntPropValue(String key, int defaultValue) {
		int value = -1;
		try {
			value = extProperties.getInt(key);
		} catch (Exception e) {
			value = properties.getInt(key, defaultValue);
		}
		return value;
	}

	/**
	 * 获得布尔值
	 * 
	 */
	public boolean getBooleanPropValue(String key, boolean defaultValue) {
		boolean value = false;
		try {
			value = extProperties.getBoolean(key);
		} catch (Exception e) {
			value = properties.getBoolean(key, defaultValue);
		}
		return value;
	}

	// /////////////////////////////////////////////

	/**
	 * 单实例
	 */
	private static ProcessContext instance;

	/**
	 * 获取实例,采用synchronized避免多线程冲突
	 * 
	 * @return
	 */
	public static synchronized ProcessContext getInstance() {
		if (instance == null) {
			instance = new ProcessContext();
		}
		return instance;
	}

	/**
	 * 私有构造函数
	 */
	private ProcessContext() {

	}

}
