package org.luis.basic.domain;

/**
 * 查询链表节点接口
 * @author guoliang.li
 */
public interface IAttributeNode {
	/**
	 * 字段名
	 * @return
	 */
	public String getName();
	
	/**
	 * 字段值
	 * @return
	 */
	public Object getValue();
	
	/**
	 * 参数名
	 * @return
	 */
	public String getParamName();
}
