package org.luis.basic.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;

public abstract class GenericsUtil {

	/**
	 * 实现将一个Object对象转换成为简单的Map<String, String>对象
	 * 
	 */
	public static Map<String, String> change(Object obj) {
		Map<String, String> map = new HashMap<String, String>();
		if (obj != null) {
			// 获得所有的字段属性
			List<Field> fields = getClassSimpleFields(obj.getClass(), true);
			for (Field field : fields) {
				String name = field.getName();
				String value = getNotNullValue(obj, name);
				map.put(name, value);
			}
		}
		return map;
	}
	
	/**
	 * 实现将一个Object对象转换成为简单的Map<String, Object>对象
	 * @param obj
	 * @return
	 */
	public static Map<String, Object> change2map(Object obj) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (obj != null) {
			// 获得所有的字段属性
			List<Field> fields = getClassSimpleFields(obj.getClass(), true);
			for (Field field : fields) {
				String name = field.getName();
				String value = getNotNullValue(obj, name);
				map.put(name, value);
			}
		}
		return map;
	}

	
	/**
	 * 实现将一个Object对象的某属性字段的值,出现异常返回""字符串
	 * 
	 */
	public static String getNotNullValue(Object instance, String fieldname) {
		String value = "";
		try {
			Object v = PropertyUtils.getProperty(instance, fieldname);
			if (v != null) {
				if (v instanceof java.util.Date) {
					// 日期格式转换
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss");
					value = sdf.format((Date) v);
				} else {
					value = v.toString();
				}
			}
		} catch (Exception e) {
		}
		return value;
	}

	/**
	 * 获取一个类，及其全部祖先的类的字段和
	 * 
	 * @param c
	 * @param ancestor
	 * @return
	 */
	public static List<Field> getClassSimpleFields(Class<?> c, boolean ancestor) {
		List<Field> fields = new ArrayList<Field>();
		setClassSimpleFields(c, fields, ancestor);
		return fields;
	}

	/**
	 * 循环设置字段
	 * 
	 * @param c
	 * @param fields
	 * @param ancestor
	 */
	private static void setClassSimpleFields(Class<?> c, List<Field> fields,
			boolean ancestor) {
		for (Field field : c.getDeclaredFields()) {
			if (!ignoreFilterField(field)) {
				fields.add(field);
			}
		}
		if (ancestor) {
			Class<?> p = c.getSuperclass();
			if (p != null) {
				setClassSimpleFields(p, fields, ancestor);
			}
		}
	}

	/**
	 * 获得指定类clz的字段，以及可能所有可能是其父类的字段之和
	 */
	public static List<Field> getClassSimpleFields(Class<?> c, Class<?>... ps) {
		// 获得所有的Field列表
		List<Field> fields = new ArrayList<Field>();
		for (Field field : c.getDeclaredFields()) {
			if (!ignoreFilterField(field)) {
				fields.add(field);
			}
		}

		for (Class<?> p : ps) {
			// 如果c是p的子类,则把p的字段也添加进来
			if (p != null && p.isAssignableFrom(c)) {
				for (Field field : p.getDeclaredFields()) {
					if (!ignoreFilterField(field)) {
						fields.add(field);
					}
				}
			}
		}
		return fields;
	}

	/**
	 * 获得指定类clz的字段，以及可能是其父类p的字段之和
	 * 
	 * @param clz
	 * @param parentClassName
	 * @return
	 */
	public static List<Field> getClassSimpleFields(Class<?> c, Class<?> p) {
		return getClassSimpleFields(c, p, null);
	}

	/**
	 * 判断字段是否是要忽略的，即复杂对象不是SimpleField，SimpleField为常用的String|Integer|Date等字段
	 * 
	 * @param field
	 * @return
	 */
	public static boolean ignoreFilterField(Field field) {
		// 如果是暂态字段,忽略之
//		if (isTransient(field)) {
//			return true;
//		}
		// 如果是序列化字段,忽略之
		if (field.getName().equalsIgnoreCase("serialVersionUID")) {
			return true;
		}
		// 如果是static字段,忽略之
		boolean staticField = Modifier.isStatic(field.getModifiers());
		if (staticField) {
			return true;
		}
		// 常规字段类型支持
		String type = field.getGenericType().toString();
		if (type.endsWith("java.lang.String")
				|| type.endsWith("java.lang.Integer")
				|| type.endsWith("java.lang.Double")
				|| type.endsWith("java.lang.Long")
				|| type.endsWith("java.lang.Boolean")
				|| type.endsWith("java.lang.Short")
				|| type.endsWith("java.util.Date")
				|| type.endsWith("java.math.BigDecimal")) {
			return false;
		}
		return true;
	}

	public static boolean isTransient(Field field) {
		Annotation[] a = field.getAnnotations();
		for (int i = 0; i < a.length; i++) {
			String type = a[i].annotationType().getName();
			if (type.indexOf("Transient") != -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
	 * GenricManager<Book>
	 * 
	 * @param clazz
	 *            The class to introspect
	 * @return the first generic declaration, or <code>Object.class</code> if
	 *         cannot be determined
	 */
	public static Class<?> getSuperClassGenricType(Class<?> clazz) {
		return getSuperClassGenricType(clazz, 0);
	}

	/**
	 * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
	 * GenricManager<Book>
	 * 
	 * @param clazz
	 *            clazz The class to introspect
	 * @param index
	 *            the Index of the generic ddeclaration,start from 0.
	 * @return the index generic declaration, or <code>Object.class</code> if
	 *         cannot be determined
	 */
	public static Class<?> getSuperClassGenricType(Class<?> clazz, int index) {

		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		return (Class<?>) params[index];
	}
}
