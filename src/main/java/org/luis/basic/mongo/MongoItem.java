package org.luis.basic.mongo;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.mongodb.DBObject;

/**
 * 封装Mongo DBObject对象，主要用于数据获取之后的显示
 * 
 */
public final class MongoItem implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9035578319183009233L;
	/**
	 * Mongo的ID,用作统计计数用
	 * 
	 */
	public String _id = "";
	/**
	 * 用作统计计数用
	 */
	public Double _value = 0.0;
	/**
	 * 系统数据的插入时间戳,该字段暂时没用
	 * 
	 */
	public long _tsi = -1;
	/**
	 * 系统数据的更新时间戳,该字段暂时没用
	 * 
	 */
	public long _tsu = -1;

	/**
	 * 内部封装DBObject对象
	 * 
	 */
	private DBObject dbo;

	/**
	 * 构造函数,封装DBObject
	 * 
	 * @param dbo
	 */
	public MongoItem(DBObject dbo) {
		this.dbo = dbo;
		this._id = dbo.get("_id").toString();
	}
	
	/**
	 * 构造函数，指定_id和_value，一般做统计结果用
	 * @param _id
	 * @param _value
	 */
	public MongoItem(String _id,Double _value) {
		this._id = _id;
		this._value = _value;
	}
	
	/**
	 * 获得Key值集合
	 * 
	 * @return
	 */
	public Set<String> keySet() {
		return dbo.keySet();
	}

	/**
	 * 获得Value对象值
	 * 
	 * @param key
	 * @return
	 */
	public Object get(String key) {
		if (key.equals("_id")) {
			return dbo.get(key).toString();
		} else {
			return dbo.get(key);
		}
	}

	/**
	 * 转化为Map对象
	 * 
	 * @return
	 */
	public Map<String, Object> toMap() {
		// 改dbo.toMap()为自己的HashMap 简化_id的获取
		Map<String, Object> item = new HashMap<String, Object>();
		Set<String> keys = keySet();
		for (String key : keys) {
			item.put(key, get(key));
		}
		return item;
		// return dbo.toMap();
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public long get_tsi() {
		return _tsi;
	}

	public void set_tsi(long _tsi) {
		this._tsi = _tsi;
	}

	public long get_tsu() {
		return _tsu;
	}

	public void set_tsu(long _tsu) {
		this._tsu = _tsu;
	}

	public Double get_value() {
		return _value;
	}

	public void set_value(Double _value) {
		this._value = _value;
	}
	
}
