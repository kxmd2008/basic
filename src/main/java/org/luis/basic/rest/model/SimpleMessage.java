package org.luis.basic.rest.model;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * 简单业务请求消息对象,可以转化成为Json数据格式,主要是定位于Rpc访问/MQTT消息传递等 参见《NGBF_移动框架接口说明》
 * 
 * 
 */
public class SimpleMessage<T> implements java.io.Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = 6693914877246416586L;

	/**
	 * 消息头对象
	 * 
	 * 
	 */
	private SimpleMessageHead head = new SimpleMessageHead();

	/**
	 * 消息单数据体，用于实现单数据 改为<String,Object>
	 * 
	 * 
	 */
	private Map<String, Object> data = null;
	/**
	 * 消息多数据体，用于实现多数据 改为<String,Object>
	 * 
	 * 
	 */
	private List<Map<String, Object>> items = null;
	/**
	 * 对象数据模型,放置对象
	 * 
	 * 
	 */
	private List<T> records = null;

	private T item;

	/**
	 * 添加条目对象
	 * 
	 * 
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
	 * 
	 * 
	 */
	public List<T> getRecords() {
		if (records == null) {
			records = new ArrayList<T>();
		}
		return records;
	}

	/**
	 * 添加Item数据对象 改为<String,Object>
	 * 
	 */
	public SimpleMessage<T> add(Map<String, Object> item) {
		if (items == null) {
			items = new ArrayList<Map<String, Object>>();
		}
		items.add(item);
		return this;
	}

	/**
	 * 获得Item数据列表对象
	 * 
	 * 
	 */
	public List<Map<String, Object>> getItems() {
		return items;
	}

	/**
	 * 构造函数，指定消息头和指定业务数据Map
	 * 
	 * 
	 */
	public SimpleMessage(SimpleMessageHead head, Map<String, Object> data) {
		this.head = head;
		this.data = data;
	}

	/**
	 * 空构造函数
	 * 
	 */
	public SimpleMessage() {
		this.head = new SimpleMessageHead("");
	}

	/**
	 * 链式API:指定空消息头
	 * 
	 * 
	 */
	public static SimpleMessage<?> blank() {
		return new SimpleMessage<Object>();
	}

	/**
	 * 链式API:指定SimpleMessageHead的消息头
	 * 
	 * 
	 */
	public SimpleMessage<?> head(SimpleMessageHead head) {
		this.head = head;
		return this;
	}

	/**
	 * 构造函数，指定消息头
	 * 
	 * 
	 */
	public SimpleMessage(SimpleMessageHead head) {
		this.head = head;
	}

	/**
	 * 构造函数,指定apiName作为消息头
	 * 
	 * @deprecated 参见blank().head()
	 */
	public SimpleMessage(String api_code) {
		this.head = new SimpleMessageHead(api_code);
	}

	/**
	 * 将对象生成Json对象数据
	 * 
	 * 
	 */
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	/**
	 * 绑定json数据为SimpleMessage,参见NtpHelper同名方法
	 * 
	 * @param json
	 * @return
	 */
	public static SimpleMessage<?> bind(String json) {
		Gson gson = new Gson();
		Type type = new TypeToken<SimpleMessage<?>>() {
		}.getType();
		return gson.fromJson(json, type);
	}

	/**
	 * 设置单数据
	 * 
	 * 
	 */
	public SimpleMessage<T> set(Map<String, Object> data) {
		this.data = data;
		return this;
	}

	/**
	 * 设置单数据
	 * 
	 * 
	 */
	public SimpleMessage<T> setStringMap(Map<String, String> data) {
		return this;
	}

	/**
	 * 设置单数据值
	 * 
	 * 
	 */
	public SimpleMessage<T> set(String key, Object value) {
		if (data == null) {
			data = new HashMap<String, Object>();
		}
		data.put(key, value);
		return this;
	}

	/**
	 * 获得单数据值
	 * 
	 * 
	 */
	public String getData(String key) {
		Object v = this.data.get(key);
		if (v == null) {
			v = "";
		}
		return v.toString();
	}

	/**
	 * 获得整体的Data单数据集
	 * 
	 * 
	 * @return
	 */
	public Map<String, Object> getData() {
		return this.data;
	}

	/**
	 * 获得Head
	 * 
	 * 
	 */
	public SimpleMessageHead getHead() {
		return head;
	}

	/**
	 * 设置Head
	 * 
	 * 
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

}
