package org.luis.basic.domain;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;

/**
 * Filter属性对象
 * 
 */
public final class FilterAttribute implements IAttributeNode {
	
	// 相等
	public final static Integer OP_EQUAL = 0;
	public final static String OP_EQUAL_LABEL = "等于";
	// 开始为
	public final static Integer OP_STARTWITH = 1;
	public final static String OP_STARTWITH_LABEL = "开始为";
	// 结束为
	public final static Integer OP_ENDWITH = 2;
	public final static String OP_ENDWITH_LABEL = "结束为";
	// LIKE
	public final static Integer OP_LIKE = 3;
	public final static String OP_LIKE_LABEL = "LIKE";
	// IN
	public final static Integer OP_IN = 4;//暂不支持
	public final static String OP_IN_LABEL = "存在于";
	// NOT IN
	public final static Integer OP_NOTIN = 41;//暂不支持
	public final static String OP_NOTIN_LABEL = "不存在于";
	// 大于
	public final static Integer OP_BIGTHAN = 5;
	public final static String OP_BIGTHAN_LABEL = "大于";
	// 大于等于
	public final static Integer OP_NOTLESSTHAN = 6;
	public final static String OP_NOTLESSTHAN_LABEL = "大于等于";
	// 小于
	public final static Integer OP_SMALLTHAN = 7;
	public final static String OP_SMALLTHAN_LABEL = "小于";
	// 小于等于
	public final static Integer OP_NOTBIGTHAN = 8;
	public final static String OP_NOTBIGTHAN_LABEL = "小于等于";
	/**
	 * 不等于
	 * @since 1.1.2.0
	 */
	public final static Integer OP_NOTEQUAL = 9;
	public final static String OP_NOTEQUAL_LABEL = "不等于";
	
	private static Map<Integer, String> operations = new HashMap<Integer, String>();

	static {
		operations.put(OP_EQUAL, OP_EQUAL_LABEL);
		operations.put(OP_STARTWITH, OP_STARTWITH_LABEL);
		operations.put(OP_ENDWITH, OP_ENDWITH_LABEL);
		operations.put(OP_LIKE, OP_LIKE_LABEL);
		operations.put(OP_IN, OP_IN_LABEL);
		operations.put(OP_BIGTHAN, OP_BIGTHAN_LABEL);
		operations.put(OP_NOTLESSTHAN, OP_NOTLESSTHAN_LABEL);
		operations.put(OP_SMALLTHAN, OP_SMALLTHAN_LABEL);
		operations.put(OP_NOTBIGTHAN, OP_NOTBIGTHAN_LABEL);
	}

	public static Map<Integer, String> getOperations() {
		return operations;
	}

	// 字段名
	private String name;
	// 字段值
	private Object value;
	// 匹配关系
	private int op = 0;

	//查询属性节点
	private AttributeNode node;

	/**
	 * 属性值的缺省获得方式,参见0.1.0.1
	 */
	private int wayGet = GET_DEFAULT;

	public FilterAttribute() {

	}

	public FilterAttribute(String name, Object value) {
		this(name, OP_EQUAL, value);
	}
	
	public FilterAttribute(String name, Object value, String paramName) {
		this(name, OP_EQUAL, value, paramName);
	}

	/**
	 * 数据库比较符号
	 */
	public FilterAttribute(String name, int op, Object value) {
		this(name, op, value, GET_DEFAULT);
	}
	
	public FilterAttribute(String name, int op, Object value, String paramName) {
		this(name, op, value, GET_DEFAULT, paramName);
	}

	/**
	 * @param name
	 * @param op
	 * @param value
	 * @param node
	 */
	public FilterAttribute(String name, Object value, AttributeNode node) {
		this(name, OP_EQUAL, value, GET_DEFAULT, node);
	}

	/**
	 * @param name
	 * @param op
	 * @param value
	 * @param node
	 */
	public FilterAttribute(String name, int op, Object value, AttributeNode node) {
		this(name, op, value, GET_DEFAULT, node);
	}

	/**
	 * 不通过类型,直接使用缺省值value
	 */
	public static int GET_DEFAULT = 0;
	/**
	 * 通过ContextKey获得对象的属性
	 */
	public static int GET_CONTEXTKEY = 1;
	/**
	 * 通过Session获得对象的属性
	 */
	public static int GET_SESSION = 2;
	/**
	 * 通过c-config的参数管理来获取对象的值
	 * 
	 */
	public static int GET_CONFIG = 3;

	/**
	 * 通过ContextKey获得对象的ID
	 * 
	 */
	public static int GET_CONTEXTOBJECTID = 4;

	/**
	 * 构造函数，指定value的获取类型
	 * 
	 * @since 0.1.0.1
	 * @param name
	 * @param op
	 * @param value
	 * @param wayGet
	 */
	public FilterAttribute(String name, int op, Object value, int wayGet) {
		Assert.assertNotNull(name);
		this.setName(name);
		this.setOp(op);
		this.setValue(value);
		this.setWayGet(wayGet);
		this.paramName = name;
	}
	
	public FilterAttribute(String name, int op, Object value, int wayGet, String paramName) {
		Assert.assertNotNull(name);
		this.setName(name);
		this.setOp(op);
		this.setValue(value);
		this.setWayGet(wayGet);
		this.paramName = paramName;
	}

	/**
	 * 
	 * @param name
	 * @param op
	 * @param value
	 * @param wayGet
	 * @param node
	 */
	public FilterAttribute(String name, int op, Object value, int wayGet,
			AttributeNode node) {
		Assert.assertNotNull(name);
		this.setName(name);
		this.setOp(op);
		this.setValue(value);
		this.setWayGet(wayGet);
		this.node = node;
	}

	public boolean hasNext() {
		return node.getNextName() != null && !"|".equals(node.getNextName());
	}
	public static final String invalidChar = ".*([';]+|(--)+).*";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public int getOp() {
		return op;
	}

	public void setOp(int op) {
		this.op = op;
	}

	public int getWayGet() {
		return wayGet;
	}

	public void setWayGet(int wayGet) {
		this.wayGet = wayGet;
	}

	public AttributeNode getNode() {
		return node;
	}

	public void setNode(AttributeNode node) {
		this.node = node;
	}

	private String paramName;
	
	@Override
	public String getParamName() {
		return paramName;
	}
	
	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

}
