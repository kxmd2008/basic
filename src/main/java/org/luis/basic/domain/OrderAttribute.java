package org.luis.basic.domain;

/**
 * order by 属性
 * 
 * @author guoliang.li
 * 
 */
public final class OrderAttribute {

	public final static String TYPE_ASC = "asc";
	public final static String TYPE_DESC = "desc";
	// 字段属性
	private String name;
	// 字段类型
	private String type;

	public OrderAttribute(String name, String type) {
		this.setName(name);
		this.setType(type);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
