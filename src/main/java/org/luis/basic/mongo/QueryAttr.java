package org.luis.basic.mongo;

import org.apache.log4j.Logger;

/**
 * 查询属性对象
 * 
 */
public final class QueryAttr {

	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger(QueryAttr.class);
	/**
	 * 相等
	 */
	public final static Integer OP_EQUAL = 0;
	public final static String OP_EQUAL_LABEL = "等于";
	/**
	 * IN
	 */
	public final static Integer OP_IN = 4;
	public final static String OP_IN_LABEL = "存在于";
	/**
	 * NOT IN
	 */
	public final static Integer OP_NOTIN = 41;
	public final static String OP_NOTIN_LABEL = "不存在于";
	/**
	 * 大于
	 */
	public final static Integer OP_BIGTHAN = 5;
	public final static String OP_BIGTHAN_LABEL = "大于";
	/**
	 * 大于等于
	 */
	public final static Integer OP_NOTLESSTHAN = 6;
	public final static String OP_NOTLESSTHAN_LABEL = "大于等于";
	/**
	 * 小于
	 */
	public final static Integer OP_SMALLTHAN = 7;
	public final static String OP_SMALLTHAN_LABEL = "小于";
	/**
	 * 小于等于
	 */
	public final static Integer OP_NOTBIGTHAN = 8;
	public final static String OP_NOTBIGTHAN_LABEL = "小于等于";
	/**
	 * 不等于
	 */
	public final static Integer OP_NOTEQUAL = 9;
	public final static String OP_NOTEQUAL_LABEL = "不等于";
	/**
	 * 开始为:暂不支持
	 */
	public final static Integer OP_STARTWITH = 1;
	public final static String OP_STARTWITH_LABEL = "开始为";
	/**
	 * 结束为:暂不支持
	 */
	public final static Integer OP_ENDWITH = 2;
	public final static String OP_ENDWITH_LABEL = "结束为";
	/**
	 * LIKE:暂不支持
	 */
	public final static Integer OP_LIKE = 3;
	public final static String OP_LIKE_LABEL = "LIKE";
	// 链接类型:[暂不支持]
	// public final static String TYPE_AND = " and ";
	// public final static String TYPE_OR = " or ";

	// 字段名
	private String name;
	// 字段值
	private Object value;
	// 匹配关系
	private int op = 0;

	public QueryAttr() {

	}

	public QueryAttr(String name, Object value) {
		this(name, OP_EQUAL, value);
	}

	public QueryAttr(String name, int op, Object value) {
		this.name = name;
		this.op = op;
		this.value = value;
	}

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

}
