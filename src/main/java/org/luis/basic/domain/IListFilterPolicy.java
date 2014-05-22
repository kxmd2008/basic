package org.luis.basic.domain;

/**
 * 过滤策略,不同的业务（ViewModel）有不同的策略，张三只能看自己的，李四能看其所在部门的，管理员能看所有的 这就是数据权限！
 * 由View层组织自己的Policy,来满足各式各样的业务
 * 
 * @author guoliang.li
 */

public interface IListFilterPolicy {

	/**
	 * 获得额外的过滤条件
	 */
	public FilterAttributes getAttrs();

}
