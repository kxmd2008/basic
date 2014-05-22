package org.luis.basic.domain;

import java.util.LinkedList;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Filter属性集合
 * 
 */
public final class FilterAttributes {

	/**
	 * 通过object注册的字段来获得缺省的查询FilterAttributes
	 * 
	 * @param names
	 * @param obj
	 * @return
	 */
	public static FilterAttributes build(String[] names, Object bean) {
		FilterAttributes attributes = new FilterAttributes();
		for (int i = 0; i < names.length; i++) {
			Object value = null;
			try {
				value = PropertyUtils.getSimpleProperty(bean, names[i]);
				if (value != null && !StringUtils.isEmpty(value.toString())) {
					attributes.add(names[i], value);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return attributes;
	}

	/**
	 * 创建
	 * 
	 * @return
	 */
	public static FilterAttributes blank() {
		FilterAttributes attributes = new FilterAttributes();
		return attributes;
	}

	/**
	 * 添加属性对象
	 * 
	 * @param attr
	 * @return
	 */
	public FilterAttributes add(IAttributeNode attr) {
		attrs.addLast(attr);
		return this;
	}

	/**
	 * 添加多支持的属性对象
	 * 
	 * @param fattrs
	 * @return
	 */
	public FilterAttributes add(FilterAttributes fattrs) {
		for (IAttributeNode attr : fattrs.getAttrs()) {
			add(attr);
		}
		return this;
	}

	/**
	 * 添加属性对象
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public FilterAttributes add(String name, Object value) {
		return add(name, FilterAttribute.OP_EQUAL, value);
	}

	/**
	 * 添加属性对象
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public FilterAttributes add(String name, String paramName, Object value) {
		return add(new FilterAttribute(name, value, paramName));
	}

	/**
	 * 添加不为null的对象,如果为null,则不加入到过滤条件
	 * 
	 * @param name
	 * @param value
	 * @return
	 */
	public FilterAttributes addIfNull(String name, Object value) {
		if (value != null) {
			return add(new FilterAttribute(name, value));
		} else {
			return this;
		}
	}

	/**
	 * 添加属性对象,对象值可能从Session中获得
	 * 
	 * @param name
	 * @param op
	 * @param value
	 * @param wayGet
	 * @return
	 */
	public FilterAttributes add(String name, int op, Object value, int wayGet) {
		return add(new FilterAttribute(name, op, value, wayGet));
	}

	/**
	 * 添加属性对象,对象值可能从Session中获得
	 * 
	 * @param name
	 * @param op
	 * @param value
	 * @param wayGet
	 * @return
	 */
	public FilterAttributes add(String name, int op, Object value, int wayGet,
			String paramName) {
		return add(new FilterAttribute(name, op, value, wayGet, paramName));
	}

	/**
	 * 添加属性对象
	 * 
	 * @param name
	 * @param op
	 * @param value
	 * @return
	 */
	public FilterAttributes add(String name, int op, Object value) {
		if (value != null) {
			if(value instanceof String){
				return add(new FilterAttribute(name, op, value.toString().trim()));
			}
			else{
				return add(new FilterAttribute(name, op, value));
			}
		} else {
			return this;
		}
	}

	/**
	 * 添加属性对象
	 * 
	 * @param name
	 * @param op
	 * @param value
	 * @return
	 */
	public FilterAttributes add(String name, int op, Object value,
			String paramName) {
		if (value != null) {
			if(value instanceof String){
				return add(new FilterAttribute(name, op, value.toString().trim(), paramName));
			}
			else{
				return add(new FilterAttribute(name, op, value, paramName));
			}
		} else {
			return this;
		}
	}

	/**
	 * 添加不为null的对象,如果为null,则不加入到过滤条件
	 * 
	 * @param name
	 * @param op
	 * @param value
	 * @return
	 */
	public FilterAttributes addIfNull(String name, int op, Object value) {
		if (value != null) {
			return add(new FilterAttribute(name, op, value));
		} else {
			return this;
		}
	}


	/**
	 * 生成ListFilter对象
	 * 
	 * @return
	 */
	public String simpleAttrToExpression() {
		StringBuffer sb = new StringBuffer();
		for (IAttributeNode attr : attrs) {
			if (attr instanceof FilterAttribute) {
				sb.append(" and ").append(attr.toString());
			}
		}
		return sb.toString();
	}

	public FilterAttributes and(FilterAttributes attrs) {
		andAttrs.add(attrs);
		return this;
	}

	public FilterAttributes or(FilterAttributes attrs) {
		orAttrs.add(attrs);
		return this;
	}

	public LinkedList<FilterAttributes> getAndAttrs() {
		return andAttrs;
	}

	public LinkedList<FilterAttributes> getOrAttrs() {
		return orAttrs;
	}

	// ////////////////////////////////////////////////////

	// 属性集合对象
	private LinkedList<IAttributeNode> attrs = new LinkedList<IAttributeNode>();
	private LinkedList<FilterAttributes> orAttrs = new LinkedList<FilterAttributes>();
	private LinkedList<FilterAttributes> andAttrs = new LinkedList<FilterAttributes>();

	public LinkedList<IAttributeNode> getAttrs() {
		return attrs;
	}

	public static void main(String[] args) {
	}
}
