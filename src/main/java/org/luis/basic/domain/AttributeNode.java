package org.luis.basic.domain;

/**
 * Filter属性节点,用来构建支持复合查询的FilterAttribute，支持and & or及()
 * 
 * @author guoliang.li
 */
public class AttributeNode {

	public AttributeNode() {
	}

	public AttributeNode(String name, String nextName,Boolean inList) {
		this.nextName = nextName;
		this.name = name;
		this.inList = inList;
	}

	//是否属于"()"内的元素
	private Boolean inList = Boolean.FALSE;
	// 下个节点字段名
	private String nextName;
	// 当前节点名
	private String name;

	public String getNextName() {
		return nextName;
	}

	public void setNextName(String nextName) {
		this.nextName = nextName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getInList() {
		return inList;
	}

	public void setInList(Boolean inList) {
		this.inList = inList;
	}

}
