package org.luis.basic.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * 去Dao实现! DAO接口，提供数据库访问操作
 * @author guoliang.li
 * @param <T>
 * @param <ID>
 */
public interface GenericDAO<T, ID extends Serializable> {
	
	public void setEntityClass(Class<T> entityClass);
	
	/**
	 * 进行hql查询
	 * @param hql
	 * @
	 */
	Object findByHql(String hql)  ;
	
	/**
	 * 进行Update或Delete操作
	 * @param hql
	 * @
	 */
	void invoke(String hql)  ;
	
	/**
	 * 进行Update或Delete操作
	 * @param sql
	 * @
	 */
	void invokeBySql(String sql)  ;
	
	/**
	 * 持久化对象.
	 * 
	 * @param <T>
	 * @param entity
	 * @return
	 */
	T save(T entity) ;

	/**
	 * 保存集合对象
	 * @param entities
	 * @
	 */
	void save(Collection<T> entities) ;
	
	/**
	 * 删除对象.
	 * 
	 * @param <T>
	 * @param entity
	 */
	void delete(T entity) ;

	/**
	 * 根据ID删除对象
	 * 
	 * @param id
	 */
	void deleteById(ID id) ;

	/**
	 * 删除对象(非优化算法,只适合少量数据的批删除)
	 * 
	 * @param id
	 */
	void deleteAll() ;

	/**
	 * 删除集合对象
	 * 
	 * @param collection
	 */
	void deleteAll(Collection<T> collection) ;

	/**
	 * 根据ID加载对象.
	 * 
	 * @param <T>
	 * @param id
	 * @return
	 */
	T get(ID id) ;

	/**
	 * 根据ID加载对象，并锁定对象.
	 * 
	 * @param <T>
	 * @param id
	 * @param lock
	 *            如果为TRUE锁定
	 * @return
	 */
	T get(ID id, boolean lock) ;

	/**
	 * 获取所有对象.
	 * 
	 * @param <T>
	 * @return
	 */
	List<T> findAll() ;

	/**
	 * 根据IListFilter来获得数据
	 * 
	 * @param filter
	 * @return
	 */
	public List<T> findByFilter(IListFilter filter) ;
	
	/**
	 * 根据IListFilter进行分页查询
	 * @param filter
	 * @param start
	 * @param pageSize
	 * @return
	 * @
	 */
	public List<T> findPaginationByFilter(IListFilter filter, int start, int pageSize) ;

	/**
	 * 根据IListFilter查询总数据数
	 * @param filter
	 * @return
	 * @
	 */
	public int findCountByFilter(IListFilter filter) ;
	/**
	 * 根据任意一个sql来查询
	 * @param sql
	 * @return
	 */
	public List<T> findBySql(String sql) ;
	
	/**
	 * 根据hql查询.
	 * 
	 * @param hql
	 * @param values
	 * @return
	 */
	List<T> find(String hql, Object... values) ;

	/**
	 * Hibernate load方式加载对象
	 * 
	 * @param <T>
	 * @param id
	 * @return
	 */
	T load(ID id) ;

	/////////////////////////////////////////////////////////////////////////////
	
	/**
	 * Hibernate load方式加载对象, 并锁定对象
	 * 
	 * @param <T>
	 * @param id
	 * @return
	 */
	T load(ID id, boolean lock) ;

	/**
	 * 根据hql查询，并锁定查询对象
	 * 
	 * @param hql
	 * @param lock
	 * @param values
	 * @return
	 */
	List<T> find(boolean lock, String hql, Object... values) ;
	
	void flush() ;

	void clear() ;

	void evict(T entity) ;
}
