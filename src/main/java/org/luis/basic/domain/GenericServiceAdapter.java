package org.luis.basic.domain;

import java.util.Collection;
import java.util.List;

/**
 * 增加IGenericService的缺省空实现,供子类集成,减少IGenericService接口增加方法而带来不必要的麻烦,
 *              增加缓冲
 */
public abstract class GenericServiceAdapter<T extends BaseEntity> implements
		IGenericService<T> {
	public void setEntityClass(Class<T> entityClass) {
		
	}
	@Override
	public boolean save(T entity) {
		return false;
	}

	@Override
	public boolean saveAll(Collection<T> entities) {
		return false;
	}

	@Override
	public boolean update(T entity) {
		return false;
	}

	@Override
	public T saveAndReturn(T entity)  {
		
		return null;
	}

	@Override
	public T get(Long id) {
		
		return null;
	}

	@Override
	public boolean deleteById(Long id) {
		
		return false;
	}
	
	@Override
	public boolean delete(T entity) {
		
		return false;
	}

	@Override
	public boolean deleteAll(Collection<T> collection) {
		
		return false;
	}

	@Override
	public boolean deleteAll(IListFilter filter) {
		
		return false;
	}

	@Override
	public List<T> findAll() {
		
		return null;
	}

	@Override
	public List<T> findByFilter(IListFilter filter) {
		
		return null;
	}

	@Override
	public List<T> findByAttributes(FilterAttributes attributes)
			 {
		
		return null;
	}
	
	@Override
	public List<T> findByAttributes(FilterAttributes attributes,boolean isSupportAopFilter)
			 {
		
		return null;
	}

	@Override
	public T findOneByFilter(IListFilter filter) {
		
		return null;
	}

	@Override
	public List<T> findBySql(String sql) {
		
		return null;
	}

	@Override
	public List<?> findByHql(String hql) {
		
		return null;
	}

	@Override
	public boolean invokeSql(String hql) {
		
		return false;
	}

	@Override
	public int invokeSql(String[] hqls) {
		
		return 0;
	}

	@Override
	public String getGenericDaoSpringKey() {
		
		return null;
	}
	
	public List<T> findPaginationByAttributes(FilterAttributes attributes, int start, int pageSize){
		return null;
	}

	@Override
	public List<T> findPaginationByFilter(IListFilter filter, int start,
			int pageSize)  {
		return null;
	}

	@Override
	public List<T> findPaginationByAttributes(FilterAttributes attributes,
			boolean isSupportAopFilter, int start, int pageSize)
			 {
		
		return null;
	}
	
	public int findCountByFilter(IListFilter filter) {
		return 0;
	}
	
	public int findCountByAttributes(FilterAttributes attributes){
		return 0;
	}
	
	public boolean invokeBySql(String sql){
		return true;
	}

}
