package org.luis.basic.mongo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.MongoClient;
import com.mongodb.ReadPreference;

@Component(IMongoClient.KEY_SPRING)
public class MyMongoClient implements IMongoClient {

	private MongoClient mongoClient = null;

	private DB mongoDb = null;

	private static Logger logger = Logger.getLogger(MyMongoClient.class);

	/**
	 * 构造函数注入,测试用
	 * 
	 * @param configuration
	 */
	public MyMongoClient(MongoConfiguration configuration) {
		this.ngbfMongoConfiguration = configuration;
		init();
	}

	/**
	 * Spring构造函数
	 */
	public MyMongoClient() {

	}

	/**
	 * Spring自动注入
	 */
	@Autowired
	private MongoConfiguration ngbfMongoConfiguration;

	@PostConstruct
	public void init() throws RuntimeException {
		// TODO Mongo集群,连接池等
		// Mongo db = new
		// Mongo("Servers=10.0.4.66:27017;ConnectTimeout=300000;ConnectionLifetime=300000;MinimumPoolSize=25;MaximumPoolSize=25;Pooled=true");
		// TODO http://blog.csdn.net/freebird_lb/article/details/7470384
		// http://api.mongodb.org/
		// TODO 读写分离...
		try {
			//是否集群
			if(ngbfMongoConfiguration.isCluster()){
				mongoClient = new MongoClient(ngbfMongoConfiguration.getAddresses());
			} else {
				mongoClient = new MongoClient(ngbfMongoConfiguration.getIp(),
						ngbfMongoConfiguration.getPort());
			}
			
			mongoDb = mongoClient.getDB(ngbfMongoConfiguration.getDbname());
			
			if(ngbfMongoConfiguration.isCluster()){
				//集群时设置优先从Master中读，Master down则从slaver读
				mongoDb.setReadPreference(ReadPreference.primaryPreferred());
			}
			
			// 增加用户名和密码验证
			boolean auth = mongoDb.authenticate(
					ngbfMongoConfiguration.getUsername(),
					ngbfMongoConfiguration.getPassword().toCharArray());
			if (!auth) {
				throw new RuntimeException("数据库连接用户名和密码不正确!");
			}
		} catch (Exception e) {
			throw new RuntimeException("初始化Mongo连接失败:" + e.getMessage());
		}
	}

	@Override
	public Set<String> getCollectionNames() {
		return mongoDb.getCollectionNames();
	}

	@Override
	public String insert(String collectionName, Map<String, Object> item) {
		try {
			Assert.notEmpty(item);
			// 获得结果集
			DBCollection dbc = get(collectionName);
			// 将Item转换成为BasicDBObject中
			BasicDBObject doc = MongoUtil.build(item);
			// 添加增加时间戳
			doc.append(IMongoClient.KEY_TSI, System.currentTimeMillis());
			// 保存操作
			dbc.save(doc);

//			String _id = doc.get("_id").toString();
			return doc.get("_id").toString();
		} catch (Exception e) {
			logger.error("插入数据失败", e);
			throw new RuntimeException("插入数据失败:" + e.getMessage());
		}
	}
	
	public DBCollection get(String collectionName) throws RuntimeException {
		return mongoDb.getCollection(collectionName);
	}

	@Override
	public void remove(String collectionName, String _id)
			throws RuntimeException {
		DBCollection dbc = get(collectionName);
		dbc.remove(new BasicDBObject(IMongoClient.KEY_ID, new ObjectId(_id)));
	}

	@Override
	public void update(String collectionName, String _id,
			Map<String, Object> item) throws RuntimeException {
		Assert.notEmpty(item);
		// 获得结果集
		DBCollection dbc = get(collectionName);
		// 转换成为BasicDBObject
		BasicDBObject dbo = MongoUtil.build(item);
		// 添加更新时间戳
		dbo.append(IMongoClient.KEY_TSU, System.currentTimeMillis());
		DBObject updateSetValue=new BasicDBObject("$set",dbo); 
		// 更新操作，upsert=true | multi=false
		dbc.update(MongoUtil.build(_id), updateSetValue, false, true);
	}

	@Override
	public MongoItem getItem(String collectionName, String _id) {
		MongoItem item = null;
		try {
			DBCollection dbc = get(collectionName);
			BasicDBObject query = MongoUtil.build(_id);
			DBObject dbo = dbc.findOne(query);
			if (dbo != null) {
				item = new MongoItem(dbo);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return item;
	}

	@Override
	public List<MongoItem> findItemsAll(String collectionName) {
		return findItems(collectionName, null);
	}

	@Override
	public List<MongoItem> findItems(String collectionName, QueryAttrs attrs) {
		DBCollection dbc = get(collectionName);
		DBCursor cursor = null;
		if (attrs != null) {
			// 通过QueryAttrs获得[查询]条件
			cursor = dbc.find(MongoUtil.getQuery(attrs));
			// 通过QueryAttrs获得[排序]条件
			BasicDBObject sort = MongoUtil.getSort(attrs);
			cursor = cursor.sort(sort).limit(attrs.getLimit());
			// 分页
			if (attrs.getCurrPage() > -1) {
				cursor.skip(attrs.getLimit() * attrs.getCurrPage());
			}
		} else {
			cursor = dbc.find();
		}
		// 遍历得到结果数据
		List<MongoItem> list = new ArrayList<MongoItem>();
		while (cursor.hasNext()) {
			list.add(new MongoItem(cursor.next()));
		}
		return list;
	}

	public Pagination findPaginationItems(String collectionName,
			QueryAttrs attrs) {
		if (attrs == null || attrs.getCurrPage() < 0) {
			throw new RuntimeException("当前页不能小于0");
		}
		DBCollection dbc = get(collectionName);
		DBCursor cursor = null;
		// 通过QueryAttrs获得[查询]条件
		cursor = dbc.find(MongoUtil.getQuery(attrs));
		int mod = cursor.count() % attrs.getLimit();
		int totalPage = 0;
		if (mod == 0) {
			totalPage = cursor.count() / attrs.getLimit();
		} else {
			totalPage = cursor.count() / attrs.getLimit() + 1;
		}
		Pagination pagination = new Pagination(totalPage, attrs.getCurrPage());
		// 通过QueryAttrs获得[排序]条件
		BasicDBObject sort = MongoUtil.getSort(attrs);
		cursor = cursor.sort(sort).limit(attrs.getLimit())
				.skip(attrs.getLimit() * (attrs.getCurrPage() - 1));
		// 遍历得到结果数据
		List<MongoItem> list = new ArrayList<MongoItem>();
		while (cursor.hasNext()) {
			list.add(new MongoItem(cursor.next()));
		}
		pagination.setItems(list);
		return pagination;
	}

	/**
	 * 进行数据统计
	 * 
	 */
	@Override
	public List<MongoItem> statistics(String collectionName, QueryAttrs attrs,
			String keyExpression, String valueExpression) {
		List<MongoItem> list = new ArrayList<MongoItem>();
		try {
			DBCollection dbc = get(collectionName);

			// Map表达式: key=this.field; value=this.numberField
			if (!keyExpression.endsWith(";")) {
				keyExpression = keyExpression + ";";
			}
			if (!valueExpression.endsWith(";")) {
				valueExpression = valueExpression + ";";
			}

			// 定义Map函数
			String mapFunc = "function(){ var key = " + keyExpression
					+ " var value=" + valueExpression + " emit(key, value);}";
			// 定义Reduce函数，缺省是SUM
			String reduceFunc = "function(key, values){return Array.sum(values);};";
			if (attrs != null) {
				int statistic = attrs.getMrStatistic();
				if (statistic == QueryAttrs.STATISTIC_AVG) {
					// 求平均 @since 2.1.1.0
					reduceFunc = "function(key, values){return Array.avg(values);};";
				}
				else{
					// DO Nothing
				}
			}

			// 暂时目标集合,Mongo API方法需要
			String outputTarget = "ngbf_output_temp";
			MapReduceCommand command = new MapReduceCommand(dbc, mapFunc,
					reduceFunc, outputTarget,
					MapReduceCommand.OutputType.REPLACE,
					MongoUtil.getQuery(attrs));
			if (attrs != null && !attrs.getSorts().isEmpty()) {
				command.setSort(MongoUtil.getSort(attrs));
			}

			// 获得结果集，解析获得List
			MapReduceOutput out = dbc.mapReduce(command);
			for (DBObject dbo : out.results()) {
				// 只存在_id 和 value
				Object oid = dbo.get("_id");
				String _id = "unknown";
				if (oid != null) {
					_id = oid.toString();
				}
				Double _value = (Double) dbo.get("value");
				list.add(new MongoItem(_id, _value));
			}

			// @since 1.8.8.0 对MR结果进行排序
			if (attrs != null && attrs.getMrValueSort() != 0) {
				Collections.sort(list,
						new MongoItemValueComparator(attrs.getMrValueSort()));
			}
		} catch (Exception e) {
			logger.error("统计失败:" + keyExpression + "\t" + valueExpression, e);
		}

		// @since 2.0.5.0 支持TopN功能,重新定义List
		list = getMrTopList(attrs, list);

		// 返回结果
		return list;
	}

	/**
	 * 获得数据大于limit数据，则返回前N条
	 * 
	 * @since 2.0.5.0 支持TopN功能,重新定义List
	 * @param attrs
	 * @param mrList
	 * @return
	 */
	private List<MongoItem> getMrTopList(QueryAttrs attrs,
			List<MongoItem> mrList) {
		List<MongoItem> temp = new ArrayList<MongoItem>();
		if (attrs != null && attrs.getLimit() > 0) {
			if (mrList.size() >= attrs.getLimit()) {
				// 获得数据大于limit数据，则返回前N条
				int size = attrs.getLimit();
				for (int i = 0; i < size; i++) {
					temp.add(mrList.get(i));
				}
			}
		} else {
			temp = mrList;
		}
		return temp;
	}

	@Override
	public List<MongoItem> statistics(String collectionName, QueryAttrs attrs,
			String keyName) {
		// 每出现的keyName都计数1
		String keyExpression = "this." + keyName;
		return statistics(collectionName, attrs, keyExpression, "1");
	}

	@Override
	public void createIndex(String collectionName, String... keys) {
		// ReferenceTo:
		// http://blog.csdn.net/allen_jinjie/article/details/9239517
		DBCollection dbc = get(collectionName);
		DBObject o = new BasicDBObject();
		for (String key : keys) {
			o.put(key, 1);
		}
		dbc.createIndex(o);
	}

	// public NgbfMongoConfiguration getNgbfMongoConfiguration() {
	// return ngbfMongoConfiguration;
	// }

	// public void setNgbfMongoConfiguration(
	// NgbfMongoConfiguration ngbfMongoConfiguration) {
	// this.ngbfMongoConfiguration = ngbfMongoConfiguration;
	// }

}

/**
 * 对MR统计之后的结果进行排序实现
 * 
 * @since 1.8.8.0
 * @author boqing.shen
 * 
 */
class MongoItemValueComparator implements Comparator<MongoItem> {

	private int sort;

	public MongoItemValueComparator(int sort) {
		this.sort = sort;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(MongoItem mi1, MongoItem mi2) {
		if (sort == QueryAttrs.SORT_DESC.intValue()) {
			return mi2.get_value().compareTo(mi1.get_value());
		} else {
			return mi1.get_value().compareTo(mi2.get_value());
		}
	}

}