package org.luis.basic.rest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * 简单业务请求消息对象,可以转化成为Json数据格式,主要是定位于Rpc访问/MQTT消息传递等 参见《NGBF_移动框架接口说明》
 * 
 * 
 */
public class SimpleMessage implements java.io.Serializable{

	public static final long serialVersionUID = 6693914877246416586L;

	/**
	 * 消息头对象
	 */
	private SimpleMessageHead head = new SimpleMessageHead();

	/**
	 * 消息单数据体，用于实现单数据
	 * 改为<String,Object> 
	 */
	private Map<String, Object> data = new HashMap<String, Object>();

	/**
	 * 消息多数据体，用于实现多数据
	 * 改为<String,Object> 
	 */
	private List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
	

	/**
	 * 添加Item数据对象
	 * 改为<String,Object> 
	 */
	public SimpleMessage add(Map<String,Object> item) {
		items.add(item);
		return this;
	}
	
	/**
	 * 获得Item数据列表对象
	 */
	public List<Map<String, Object>> getItems() {
		return items;
	}

	/**
	 * 构造函数，指定消息头和指定业务数据Map
	 */
	public SimpleMessage(SimpleMessageHead head, Map<String, Object> data) {
		this.head = head;
		this.data = data;
	}

	/**
	 * 空构造函数
	 */
	public SimpleMessage() {

	}

	/**
	 * 构造函数，指定消息头
	 */
	public SimpleMessage(SimpleMessageHead head) {
		this.head = head;
	}

	/**
	 * 构造函数,指定apiName作为消息头
	 * 
	 * @param apiName
	 */
	public SimpleMessage(String api_code) {
		this.head = new SimpleMessageHead(api_code);
	}

	/**
	 * 将对象生成Json对象数据
	 * 
	 * @return
	 */
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson(this);
	}

	/**
	 * 通过json字符串转化成为SimpleMessage对象
	 * 
	 * @param json
	 * @return
	 */
	public static SimpleMessage bind(String json) {
		try {
			Gson gson = new Gson();
			return (SimpleMessage) gson.fromJson(json, SimpleMessage.class);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 设置单数据
	 * 
	 * @param data
	 * @return
	 */
	public SimpleMessage set(Map<String, Object> data) {
		this.data = data;
		return this;
	}
	
	
	
	public SimpleMessage setStringMap(Map<String, String> data) {
		// todo
		
		return this;
	}

	/**
	 * 设置单数据值
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public SimpleMessage set(String key, Object value) {
		this.data.put(key, value);
		return this;
	}

	/**
	 * 获得单数据值
	 * 
	 * @param key
	 * @return
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
	 * @return
	 */
	public Map<String, Object> getData() {
		return this.data;
	}

	public SimpleMessageHead getHead() {
		return head;
	}

	public void setHead(SimpleMessageHead head) {
		this.head = head;
	}
	public static void main(String[] args){
		String json = "{\"data\":{\"id\":\"111\",\"dep\":\"222\"}}" ;
		SimpleMessage s = SimpleMessage.bind(json) ;
		System.out.println(s.getData("id"));
	}
}
