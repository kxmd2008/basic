package org.luis.basic.mongo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import org.bson.types.ObjectId;
import org.luis.basic.util.SpringContextFactory;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public final class MongoUtil {

	/**
	 * QueryAttrs转换成为BasicDBObject，用于排序
	 */
	public static BasicDBObject getSort(QueryAttrs attrs) {
		BasicDBObject sort = new BasicDBObject();
		if (attrs != null) {
			for (QueryAttr attr : attrs.getSorts()) {
				sort.append(attr.getName(), attr.getValue());
			}
		}
		return sort;
	}
	
	/**
	 * QueryAttrs转换成为BasicDBObject，用于查询
	 */
	public static BasicDBObject getQuery(QueryAttrs attrs) {
		BasicDBObject query = new BasicDBObject();
		if (attrs != null) {
			for (QueryAttr attr : attrs.getAttrs()) {
				String key = attr.getName();
				Object value = attr.getValue();
				int op = attr.getOp();
				// http://docs.mongodb.org/manual/reference/operator/query/
				// 支持OP判定 @since 1.8.3.0
				if (op == QueryAttr.OP_STARTWITH) {
					Pattern pattern = Pattern.compile("^" + value);
					query.append(key, pattern);
				} else if (op == QueryAttr.OP_ENDWITH) {
					Pattern pattern = Pattern.compile(value + "$");
					query.append(key, pattern);
				} else if (op == QueryAttr.OP_LIKE) {
					Pattern pattern = Pattern.compile(value.toString());
					query.append(key, pattern);
				} else if (op == QueryAttr.OP_SMALLTHAN) {
					query.append(key, new BasicDBObject("$lt", value));
				} else if (op == QueryAttr.OP_NOTBIGTHAN) {
					query.append(key, new BasicDBObject("$lte", value));
				} else if (op == QueryAttr.OP_NOTLESSTHAN) {
					query.append(key, new BasicDBObject("$gte", value));
				} else if (op == QueryAttr.OP_BIGTHAN) {
					query.append(key, new BasicDBObject("$gt", value));
				} else if (op == QueryAttr.OP_IN) {
					// 必须是Collection集合
					BasicDBList values = MongoUtil.getDbList(value);
					query.append(key, new BasicDBObject("$in", values));
				} else if (op == QueryAttr.OP_NOTIN) {
					// 必须是Collection集合
					BasicDBList values = MongoUtil.getDbList(value);
					query.append(key, new BasicDBObject("$nin", values));
				} else if(op == QueryAttr.OP_NOTEQUAL){
					query.append(key, new BasicDBObject("$ne" , value));
				}else {
					query.append(key, value);
				}
			}
		}
		return query;
	}
	
	
	
	/**
	 * 通过值，转换成为BasicDBList,用于In，NotIn查询
	 */
	public static BasicDBList getDbList(Object value){
		BasicDBList values = new BasicDBList();
		if (value instanceof Collection) {
			@SuppressWarnings({ "unchecked", "rawtypes" })
			Collection<Object> cv = (Collection) value;
			for (Object o : cv) {
				values.add(o);
			}
		}
		return values;
	}
	
	/**
	 * 获得INgbfMongoClient对象,減少GenericMongoService的集成
	 * @return
	 */
	public static synchronized IMongoClient getClient(){
		return (IMongoClient) SpringContextFactory
				.getSpringBean(IMongoClient.KEY_SPRING);
	}
	
	/**
	 * 根据DBObject对象创建Map对象
	 * 
	 * @since 1.8.0.0
	 */
	public static Map<String,Object> build(DBObject dbo) {
		Map<String,Object> map = new HashMap<String,Object>();
		Set<String> set = dbo.keySet();
		for (String key : set) {
			map.put(key, dbo.get(key));
		}
		return map;
	}
	
	/**
	 * 根据Map对象创建BasicDBObject
	 * 
	 */
	public static BasicDBObject build(Map<String, Object> item) {
		BasicDBObject doc = new BasicDBObject();
		Set<Entry<String, Object>> set = item.entrySet();
		for (Entry<String, Object> entry : set) {
			doc.append(entry.getKey(), entry.getValue());
		}
		return doc;
	}

	/**
	 * 再已有的BasicDBObject對象上追加Map信息
	 * 如果Map对象中的值有null的现象，则删除field
	 */
	public static BasicDBObject build(BasicDBObject dbo,
			Map<String, Object> item) {
		Set<Entry<String, Object>> set = item.entrySet();
		for (Entry<String, Object> entry : set) {
			if(entry.getValue()==null){
				dbo.removeField(entry.getKey());
			}
			else{
				dbo.append(entry.getKey(), entry.getValue());
			}
		}
		return dbo;
	}

	/**
	 * 根据_id对象创建BasicDBObject，主要是用于基于主键的对象定位
	 * 
	 */
	public static BasicDBObject build(String _id) {
		return new BasicDBObject("_id", new ObjectId(_id));
	}
	
	

}
