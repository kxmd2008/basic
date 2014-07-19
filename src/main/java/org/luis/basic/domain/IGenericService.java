package org.luis.basic.domain;

import java.util.Collection;
import java.util.List;

/**
 * 支持基本对象的获得
 * 
 * @param <T>
 */
public interface IGenericService<T extends BaseEntity> {

	public static final String KEY_SPRING = "genericService";
	
	public void setEntityClass(Class<T> entityClass);
	/**
	 * 保存,返回操作结果
	 * 
	 * @param entity
	 */
	public boolean save(T entity);
	
	/**
	 * 保存集合对象,返回操作结果
	 * @param entities
	 * @return
	 */
	public boolean saveAll(Collection<T> entities);
	
	/**
	 * 更新对象
	 * @param entity
	 * @return
	 */
	public boolean update(T entity);

	/**
	 * 游离态对象保存，返回持久化对象，操作失败返回null
	 * 
	 * @param entity
	 * @return
	 */
	public T saveAndReturn(T entity) throws RuntimeException;

	/**
	 * 获得持久化对象,如果没有返回null
	 * 
	 * @param id
	 * @return
	 */
	public T get(Long id);

	/**
	 * 根据ID删除对象,返回操作结果
	 * 
	 * @param id
	 */
	public boolean deleteById(Long id);
	
	
	/**
	 * 依据对象直接删除，不再通过ID进行删除
	 * @param entity
	 * @return
	 */
	public boolean delete(T entity);

	/**
	 * 删除整个结果集,返回操作结果
	 * 
	 * @param collection
	 */
	public boolean deleteAll(Collection<T> collection);

	/**
	 * 删除根据IListFilter查找到的所有的结果集，返回操作结果
	 * 
	 * @param filter
	 */
	public boolean deleteAll(IListFilter filter);

	/**
	 * 查找所有对象,查找失败的话，返回EmptyList
	 * 
	 * @return
	 */
	public List<T> findAll();


	/**
	 * 根据IListFilter接口类实现数据的查询,查找失败的话，返回EmptyList
	 * 
	 * @param filter
	 * @return
	 */
	public List<T> findByFilter(IListFilter filter);
	
	
	
	/**
	 * 根据FilterAttributes接口类实现查询据数总数
	 * @param attributes
	 * @return
	 * @throws DomainException
	 */
	public int findCountByAttributes(FilterAttributes attributes) throws RuntimeException;
	
	/**
	 * 根据IListFilter查询据数总数
	 * @param filter
	 * @return
	 * @throws DomainException
	 */
	public int findCountByFilter(IListFilter filter) throws RuntimeException;
	
	/**
	 * 根据IListFilter进行分页查询
	 * @param filter
	 * @param start
	 * @param pageSize
	 * @return
	 * @throws DomainException
	 */
	public List<T> findPaginationByFilter(IListFilter filter, int start, int pageSize) throws RuntimeException;

	/**
	 * 根据FilterAttributes接口类实现数据的查询,查找失败的话，返回EmptyList
	 * 
	 * @param attributes
	 * @return
	 */
	public List<T> findByAttributes(FilterAttributes attributes) throws RuntimeException;
	
	public T findOneByFilter(FilterAttributes attrs);

	/**
	 * 根据FilterAttributes接口类 + listFilterPolicy实现数据的查询,查找失败的话，返回EmptyList
	 * @param filter
	 * @param listFilterPolicy 从View端进行构建
	 * @since 1.2.4.0
	 * @return
	 */
	public List<T> findByAttributes(FilterAttributes attributes,IListFilterPolicy listFilterPolicy);
	
	/**
	 * 根据FilterAttributes进行分页查询
	 * @param attributes
	 * @param start
	 * @param pageSize
	 * @return
	 * @throws RuntimeException
	 */
	public List<T> findPaginationByAttributes(FilterAttributes attributes, int start, int pageSize) throws RuntimeException;
	
	public List<T> findByAttributes(FilterAttributes attributes, boolean isSupportAopFilter) throws RuntimeException;
	
	/**
	 * 根据FilterAttributes进行分页查询
	 * @param attributes
	 * @param isSupportAopFilter
	 * @param start
	 * @param pageSize
	 * @return
	 * @throws RuntimeException
	 */
	public List<T> findPaginationByAttributes(FilterAttributes attributes, boolean isSupportAopFilter, int start, int pageSize) throws RuntimeException;
	
	/**
	 * 根据IListFilter接口类实现数据的查询，返回第一个,没有返回null
	 * 
	 * @param filter
	 * @return
	 */
	public T findOneByFilter(IListFilter filter);

	/**
	 * 根据sql来查找对象,查找失败的话，返回EmptyList
	 * 
	 * @param sql
	 * @return
	 */
	public List<T> findBySql(String sql);
	
	/**
	 * 执行HQL的查询操作
	 * @param hql
	 * @return
	 */
	public List<?> findByHql(String hql);

	/**
	 * 执行HQL的Update或者Delete操作
	 * @since 0.3.4.3
	 * @param hql
	 * @return
	 */
	public boolean invokeSql(String hql);
	
	/**
	 * 执行HQL的Update或者Delete操作
	 * @param hql
	 * @return
	 */
	public int invokeSql(String[] hqls);
	
	/**
	 * 进行Update或Delete操作
	 * @param sql
	 * @throws DomainException
	 */
	public boolean invokeBySql(String sql);
	
	/**
	 * 各子实现返回各自Dao在Spring的注册名称
	 */
	public String getGenericDaoSpringKey();

}
