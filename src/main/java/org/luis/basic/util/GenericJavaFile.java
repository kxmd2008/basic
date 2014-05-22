package org.luis.basic.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class GenericJavaFile {
	public static void main(String[] args) {
		String path = "C:/Develop/workspace/sainteclaires-base/src/main/java/com/sainteclaires/base/bean";
		GenericServiceFacotory(path, "sainteclaires-base");
	}
	
	private static void GenericServiceFacotory(String path, String project){
//		InputStream is = GenericJavaFile.class.getResourceAsStream(resourceName);
		String temp = GenericJavaFile.class.getResource("").getPath();
		String pathStr = temp.substring(0, temp.indexOf("classes")) + "classes";
		System.out.println(pathStr);
		String serviceFile = path + "/service/ServiceFactory.java";
		Map<String, Object> map = new HashMap<String, Object>();
		List<String> beanNames = getFileNames(path);
		GenericJavaFile policy = new GenericJavaFile();
		map.put("package", "com.sainteclaires.base.bean");
		map.put("names", beanNames);
		map.put("policy", policy);
		TemplateUtil.create("ServiceFactroy.vm", map, pathStr, serviceFile);
		
	}
	
	private static List<String> getFileNames(String path){
		File file = new File(path);
		List<String> list = new ArrayList<String>();
		if(file.exists()){
			File[] files = file.listFiles();
			for (File file2 : files) {
				if(file2.getName().endsWith("java")){
					String jname = file2.getName().replace(".java", "");
					list.add(jname);
				}
			}
			
		}
		return list;
	}
	
	public String toLow(String name){
		return (name.charAt(0)+"").toLowerCase() + name.substring(1);
	}
}
