package org.luis.basic.domain;

/**
 * 声明Fielter的结果集，可以被传入到Find中区
 * 
 */
public interface IListFilter {

	/**
	 * 组合模式，可以继续添加IListFilter对象
	 * 
	 * @param filter
	 */
	public void append(IListFilter filter);

	/**
	 * 判断该IListFilter是否可以再append过滤器
	 * AOP拦截中(针对IListFilter再append过滤器),参见DomainFilterEventInterceptor
	 * 但是有的时候IListFilter不希望AOP拦截,即再后挂filter,所以有此接口
	 * @return
	 */
	public boolean isCanAppendable();
	
	/**
	 * 设置是否可以后挂filter
	 * @param canAppendable
	 */
	public void setCanAppendable(boolean canAppendable);
	
	/**
	 * 获得构建ListFilter的FilterAttributes
	 * @return
	 */
	public FilterAttributes getAttrs();
	
	/**
	 * 获得构建ListFilter的OrderAttributes
	 * @return
	 */
	public OrderAttributes getOrderAttrs();

}
