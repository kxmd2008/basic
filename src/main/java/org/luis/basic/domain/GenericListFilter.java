package org.luis.basic.domain;


/**
 * IListFilter的缺省实现,具体实现交给FilterRule
 */
public class GenericListFilter extends AbstractListFilter {

	/**
	 * 创建GenericListFilter对象,屏蔽new方法
	 * @param attrs
	 * @return
	 */
	public static GenericListFilter build(FilterAttributes attrs){
		return new GenericListFilter(attrs);
	}
	
	public GenericListFilter() {
	}

	/**
	 * 创建基于FilterAttributes的过滤器
	 * @param attrs
	 */
	public GenericListFilter(FilterAttributes attrs) {
		this.attrs = attrs;
	}
	
}
