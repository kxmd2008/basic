package org.luis.basic.mongo;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.DBCollection;

public interface IMongoClient {

	String KEY_SPRING = "ngbfMongoClient";

	/**
	 * 主键字段
	 * 
	 */
	String KEY_ID = "_id";
	/**
	 * 插入时间戳字段
	 * 
	 */
	String KEY_TSI = "_tsi";
	/**
	 * 更新时间戳字段
	 * 
	 */
	String KEY_TSU = "_tsu";

	/**
	 * 创建索引
	 * 
	 * @param keys
	 */
	void createIndex(String collectionName, String... keys);

	/**
	 * 一个Collection就是一张表
	 * 
	 */
	Set<String> getCollectionNames();

	/**
	 * 根据collectionName来获取DBCollection对象
	 * 
	 * @param collectionName
	 * @return
	 */
	DBCollection get(String collectionName);

	/**
	 * 插入Map对象数据
	 * 
	 * 改为返回Mongo条目的_id,插入失败则返回null
	 */
	String insert(String collectionName, Map<String, Object> item)
			throws RuntimeException;

	/**
	 * 已知_id，进行删除
	 * 
	 */
	void remove(String collectionName, String _id) throws RuntimeException;

	/**
	 * 根据已知的_id进行指定的数据修改 如果原有的Key不再新的Map中，则被冲掉了
	 * 
	 */
	void update(String collectionName, String _id, Map<String, Object> item)
			throws RuntimeException;

	/**
	 * 转换成为MongoItem对象
	 */
	MongoItem getItem(String collectionName, String _id);

	/**
	 * 转换成为MongoItem对象对象
	 */
	List<MongoItem> findItems(String collectionName, QueryAttrs query);
	
	/**
	 * 获取分页数据
	 * @param collectionName
	 * @param attrs
	 * @return
	 */
	public Pagination findPaginationItems(String collectionName, QueryAttrs attrs);

	/**
	 * 获得全部MongoItem结果集
	 * 
	 */
	List<MongoItem> findItemsAll(String collectionName);

	/**
	 * 对collectionName条件为query的某字段做数据统计合计 keyExpression = this.keyName or var
	 * key = ''; if(this.type=='S' & this.fileNum<100){key='SL'}
	 * else{key='OTHERS'}; valueExpression = "1" or "this.numberValue"
	 * 返回get_id,get_value
	 * 
	 */
	List<MongoItem> statistics(String collectionName, QueryAttrs queryAttrs,
			String keyExpression, String valueExpression);
	
	/**
	 * 统计count，方法实现同statisticsCount
	 * @param collectionName
	 * @param queryAttrs
	 * @param keyName
	 * @return
	 */
	List<MongoItem> statistics(String collectionName,
			QueryAttrs queryAttrs, String keyName);
	
}
