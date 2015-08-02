package org.luis.basic.util;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class DictManager {
	
	private Map<String, Map<String, String>> map = new HashMap<String, Map<String, String>>();

	@SuppressWarnings("unchecked")
	private void init() {
		String path = getClass().getClassLoader().getResource("/").getPath() + "Dict.xml";
		File filePath = new File(path);
		File[] files = filePath.listFiles();
		for (File file : files) {
			SAXReader saxReader = new SAXReader();
			Document document;
			try {
				document = saxReader.read(file);
				Element root = document.getRootElement();
				List<Element> list = root.elements();
				for (Element dict : list) {
					String code = trim(dict.attributeValue("code"));
					if(map.get(code) == null){
						map.put(code, new HashMap<String, String>());
					}
					List<Element> items = dict.elements();
					for (Element item : items) {
						String label = trim(item.attributeValue("label"));
						String value = trim(item.attributeValue("value"));
						map.get(code).put(value, label);
					}
				}
			} catch (DocumentException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * 获取dictitem的label
	 * @param code
	 * @param value
	 * @return
	 */
	public String getLabel(String code, String value){
		return map.get(code).get(value);
	}

	public static DictManager getInstance() {
		return instance;
	}

	private static final DictManager instance = new DictManager();

	private DictManager() {
		init();
	}
	
	private static String trim(String s){
		if(s != null){
			return s.trim();
		}
		return "";
	}

}
