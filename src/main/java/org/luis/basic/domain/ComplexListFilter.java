package org.luis.basic.domain;


/**
 * 支持Filter和Order排序的Filter对象
 * @author guoliang.li
 *
 */
public class ComplexListFilter extends AbstractListFilter {

	private IListFilter subFilter;
	
	/**
	 * 创建简单的基于FilterAttributes的过滤器
	 * @param attrs
	 */
	public ComplexListFilter(FilterAttributes attrs) {
		this(null,attrs,null);
	}
	
	/**
	 * 注册基于FilterAttributes和OrderAttributes组织城的IFilter
	 * @param filters
	 * @param orders
	 */
	public ComplexListFilter(FilterAttributes attrs,OrderAttributes orders) {
		this(null,attrs,orders);
	}
	
	/**
	 * 注册基于IListFilter,FilterAttributes和OrderAttributes组织城的IFilter
	 * IListFilter subFilter没有Order by元素
	 * @param subFilter
	 * @param attrs
	 * @param orders
	 */
	public ComplexListFilter(IListFilter subFilter,FilterAttributes attrs,OrderAttributes orders) {
		this.attrs = attrs;
		this.orderAttrs = orders;
		this.subFilter = subFilter;
	}
	
	@Override
	public void append(IListFilter filter) {
		this.filter = filter;
	}
	
	private IListFilter filter;
	
}
