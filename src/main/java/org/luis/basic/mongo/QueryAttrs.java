package org.luis.basic.mongo;

import java.util.LinkedList;

/**
 * 查询属性对象集合
 * 
 */
public final class QueryAttrs {

	/**
	 * 增序排序
	 * 
	 */
	public final static Integer SORT_ASC = 1;
	/**
	 * 逆序排序
	 * 
	 */
	public final static Integer SORT_DESC = -1;
	
	/**
	 * MR统计方式：求和
	 */
	public final static Integer STATISTIC_SUM = 0;
	/**
	 * MR统计方式：求平均
	 */
	public final static Integer STATISTIC_AVG = 1;
//	/**
//	 * 统计方式：求MAX
//	 */
//	public final static Integer STATISTIC_MAX = 2;
	/**
	 * MR统计方式:缺省是求和
	 */
	private int mrStatistic = STATISTIC_SUM;
	
	/**
	 * 统计方式
	 * @param statistic
	 * @return
	 */
	public QueryAttrs mrStatistic(int statistic) {
		this.mrStatistic = statistic;
		return this;
	}
	
	/**
	 * 获得统计方式
	 * @return
	 */
	public int getMrStatistic() {
		return mrStatistic;
	}


	/**
	 * 返回最近的几个，0:表示返回全部
	 */
	private int limit = 0;
	
	/**
	 * 当前页[0-N]
	 */
	private int currPage = -1;

	/**
	 * 定义返回的条目数
	 * 
	 */
	public QueryAttrs limit(int limit) {
		this.limit = limit;
		return this;
	}

	/**
	 * @return
	 */
	public int getLimit() {
		return limit;
	}
	
	/**
	 * 设置当前页
	 * @param currPage
	 * @return
	 */
	public QueryAttrs currPage(int currPage){
		this.currPage =  currPage;
		return this;
	}
	
	/**
	 * @return
	 */
	public int getCurrPage(){
		return currPage;
	}

	/**
	 * 创建空的查询条件
	 * 
	 */
	public static QueryAttrs blank() {
		QueryAttrs attributes = new QueryAttrs();
		return attributes;
	}

	/**
	 * 添加属性对象
	 * 
	 */
	public QueryAttrs add(QueryAttr attr) {
		attrs.addLast(attr);
		return this;
	}

	/**
	 * 添加多支持的属性对象
	 * 
	 */
	public QueryAttrs add(QueryAttrs fattrs) {
		for (QueryAttr attr : fattrs.getAttrs()) {
			add(attr);
		}
		return this;
	}

	/**
	 * 添加属性对象
	 */
	public QueryAttrs add(String name, Object value) {
		return add(new QueryAttr(name, value));
	}

	/**
	 * 添加不为null的对象,如果为null,则不加入到过滤条件
	 */
	public QueryAttrs addIfNull(String name, Object value) {
		if (value != null) {
			return add(new QueryAttr(name, value));
		} else {
			return this;
		}
	}

	/**
	 * 添加属性对象
	 */
	public QueryAttrs add(String name, int op, Object value) {
		return add(new QueryAttr(name, op, value));
	}

	/**
	 * 增加Sort支持:升序排序
	 */
	public QueryAttrs asc(String name) {
		sorts.addLast(new QueryAttr(name, SORT_ASC));
		return this;
	}

	/**
	 * 增加Sort支持:降序序排序
	 */
	public QueryAttrs desc(String name) {
		sorts.addLast(new QueryAttr(name, SORT_DESC));
		return this;
	}

	/**
	 * MR之后的Value排序，0:表示不排序
	 */
	private int mrValueSort = 0;

	/**
	 * 设定MR的Value排序结果，参见SORT_ASC|SORT_DESC
	 */
	public QueryAttrs mrValueSort(int sort) {
		this.mrValueSort = sort;
		return this;
	}
	
	/**
	 * 设定MR的Value排序结果SORT_DESC
	 * @return
	 */
	public QueryAttrs mrValueDesc() {
		this.mrValueSort = SORT_DESC;
		return this;
	}
	
	/**
	 * 设定MR的Value排序结果SORT_ASC
	 * @return
	 */
	public QueryAttrs mrValueAsc() {
		this.mrValueSort = SORT_ASC;
		return this;
	}

	
	
	/**
	 * 返回MR排序方式
	 * 
	 */
	public int getMrValueSort() {
		return mrValueSort;
	}

	// ////////////////////////////////////////////////////

	/**
	 * 查询属性集合对象
	 */
	private LinkedList<QueryAttr> attrs = new LinkedList<QueryAttr>();
	/**
	 * 排序属性集合对象
	 * 
	 */
	private LinkedList<QueryAttr> sorts = new LinkedList<QueryAttr>();

	public LinkedList<QueryAttr> getAttrs() {
		return attrs;
	}

	public LinkedList<QueryAttr> getSorts() {
		return sorts;
	}

}
