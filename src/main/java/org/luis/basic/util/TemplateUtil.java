package org.luis.basic.util;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

public class TemplateUtil {
	
	private static String mergeByFileResource(String templateName,
			Map<String, Object> map, String templatePath) {
		// 填充内容
		StringWriter writer = new StringWriter();
		try {
			// 初始化上下文
			Properties p = TemplateUtil.getFileLoaderProperties();
			p.setProperty(Velocity.FILE_RESOURCE_LOADER_PATH, templatePath);
			Velocity.init(p);
			// 获得模板
			Template template = Velocity.getTemplate(templateName);
			if (template == null) {
				throw new Exception("Template is null!");
			}

			// Merge操作
			return merge(map, template, writer);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error on velocity merge!");
		} finally {
			try {
				writer.close();
			} catch (Exception e2) {

			}
		}
	}

	public static void create(String templateName, Map<String, Object> map,
			String templatePath, String sourceFilePath) {
		String text = mergeByFileResource(templateName, map, templatePath);
		FileUtil.writeInfoToFile(text, sourceFilePath);
	}
	
	/**
	 * 将字符串进行转化，以支持中文
	 * @param content
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String getRTFCodeString(String content) throws UnsupportedEncodingException {
		   byte[] b = content.getBytes("GB2312");
		   StringBuffer sb = new StringBuffer();
		   for (int i = 0; i < b.length; i++) {
			   sb.append("\\\'").append(Integer.toHexString((b[i] & 0x000000ff) | 0xffffff00).substring(6).toUpperCase());
		   }
		return sb.toString();
	}
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static String merge(Map<String, Object> map, Template template,
			StringWriter writer) throws Exception {
		VelocityContext context = new VelocityContext();
		Set<String> set = map.keySet();
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String key = iterator.next();
			Object object = map.get(key);
			context.put(key, object);
		}
		template.merge(context, writer);
		return writer.getBuffer().toString();
	}

	public static Properties getStringLoaderProperties() {
		Properties p = new Properties();
		p.setProperty("resource.loader", "string");
		p.setProperty("resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.StringResourceLoader");
		return p;
	}

	/**
	 * 或缺缺省的VelocityEngine所需要的属性，依据
	 * 
	 * @return
	 */
	public static Properties getFileLoaderProperties() {
		Properties ps = new Properties();
		ps.setProperty("file.resource.loader.description",
				"Velocity File Resource Loader");
		ps.setProperty("file.resource.loader.class",
				"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
		// ps.setProperty("file.resource.loader.path", getParams()
		// .getTemplateDir());
		ps.setProperty("file.resource.loader.cache", "true");
		ps.setProperty("file.resource.loader.modificationCheckInterval", "1");
		// ps.setProperty("input.encoding", "GBK");
		// ps.setProperty("output.encoding", "GBK");
		ps.setProperty("input.encoding", "UTF-8");
		ps.setProperty("output.encoding", "UTF-8");
		ps.setProperty("directive.foreach.counter.name", "velocityCount");
		ps.setProperty("directive.foreach.counter.initial.value", "0");
		return ps;
	}
}
