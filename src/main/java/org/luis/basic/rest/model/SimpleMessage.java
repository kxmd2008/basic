package org.luis.basic.rest.model;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

/**
 * 简单业务请求消息对象,可以转化成为Json数据格式
 */
public class SimpleMessage<T> implements java.io.Serializable {

	public static final long serialVersionUID = 6693914877246416586L;

	/**
	 * 消息头对象
	 */
	private SimpleMessageHead head;

	/**
	 * 对象数据模型,放置对象
	 */
	private List<T> records = null;

	private T item;

	/**
	 * 添加条目对象
	 * @param record
	 * @return
	 */
	public SimpleMessage<T> addRecord(T record) {
		if (records == null) {
			records = new ArrayList<T>();
		}
		records.add(record);
		return this;
	}

	/**
	 * 获得所有条目对象
	 */
	public List<T> getRecords() {
		if (records == null) {
			records = new ArrayList<T>();
		}
		return records;
	}

	/**
	 * 空构造函数
	 * 
	 */
	public SimpleMessage() {
		this.head = new SimpleMessageHead();
	}

	/**
	 * 链式API:指定空消息头
	 */
	public static SimpleMessage<?> blank() {
		return new SimpleMessage<Object>();
	}

	/**
	 * 链式API:指定SimpleMessageHead的消息头
	 */
	public SimpleMessage<?> head(SimpleMessageHead head) {
		this.head = head;
		return this;
	}

	/**
	 * 构造函数，指定消息头
	 */
	public SimpleMessage(SimpleMessageHead head) {
		this.head = head;
	}

	/**
	 * 将对象生成Json对象数据
	 */
	public String toJson() {
		JSONObject json = JSONObject.fromObject(this);
		return json.toString();
	}

	/**
	 * 获得Head
	 */
	public SimpleMessageHead getHead() {
		return head;
	}

	/**
	 * 设置Head
	 */
	public void setHead(SimpleMessageHead head) {
		this.head = head;
	}

	public T getItem() {
		return item;
	}

	public void setItem(T item) {
		this.item = item;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}

}
