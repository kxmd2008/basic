package org.luis.basic.domain;

import java.util.LinkedList;

/**
 * Filter属性集合
 * 
 */
public final class OrderAttributes {

	/**
	 * 创建
	 */
	public static OrderAttributes blank() {
		OrderAttributes attributes = new OrderAttributes();
		return attributes;
	}

	/**
	 * 添加属性对象
	 */
	public OrderAttributes add(OrderAttribute attr) {
		attrs.addLast(attr);
		return this;
	}

	/**
	 * 添加属性对象
	 */
	public OrderAttributes add(String name, String type) {
		attrs.addLast(new OrderAttribute(name, type));
		return this;
	}

	/**
	 * 添加属性对象
	 */
	public OrderAttributes add(String name) {
		attrs.addLast(new OrderAttribute(name, OrderAttribute.TYPE_ASC));
		return this;
	}

	// 属性集合对象
	private LinkedList<OrderAttribute> attrs = new LinkedList<OrderAttribute>();
	
	public String toString(){
		StringBuilder sb = new StringBuilder(" order by ");
		for (OrderAttribute oa : attrs) {
			sb.append(" ").append(oa.getName()).append(" ").append(oa.getType()).append(",");
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}
	
	public static void main(String[] args) {
		OrderAttributes oa = OrderAttributes.blank().add("test").add("test2");
		System.out.println(oa.toString());
	}

}
