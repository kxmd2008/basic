package org.luis.basic.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

/**
 * 数据库的Service实现，实现此类不需要再写DAO层
 * 
 * @author guoliang.li
 * 
 * @param <T>
 */
@Service(IGenericService.KEY_SPRING)
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class GenericServiceImpl<T extends BaseEntity> implements
		IGenericService<T> {
	
	public Class<T> entityClass;

	public GenericServiceImpl() {
	}

	/**
	 * 添加Domain对象保存注解
	 * 
	 */
	@Override
	public boolean save(T entity) {
		getGenericDao().save(entity);
		return true;
	}

	@Override
	public boolean saveAll(Collection<T> entities) {
		getGenericDao().save(entities);
		return true;
	}

	@Override
	public boolean update(T entity) {
		getGenericDao().save(entity);
		return true;
	}

	@Override
	public T saveAndReturn(T entity) {
		getGenericDao().save(entity);
		return entity;
	}

	@Override
	public T get(Long id) {
		try {
			return getGenericDao().get(id);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean deleteById(Long id) {
		getGenericDao().deleteById(id);
		return true;
	}

	@Override
	public boolean delete(T entity) {
		getGenericDao().delete(entity);
		return true;
	}

	@Override
	public List<T> findAll() {
		try {
			return getGenericDao().findAll();
		} catch (Exception e) {
			return new ArrayList<T>();
		}
	}

	@Override
	public List<T> findByFilter(IListFilter filter) {
		try {
			return getGenericDao().findByFilter(filter);
		} catch (Exception e) {
			return new ArrayList<T>();
		}
	}

	public int findCountByAttributes(FilterAttributes attributes) {
		try {
			IListFilter filter = new GenericListFilter(attributes);
			// 设置是否可以后挂IListFilter
			filter.setCanAppendable(Boolean.FALSE);
			return findCountByFilter(filter);
		} catch (Exception e) {
			return 0;
		}
	}

	public int findCountByFilter(IListFilter filter) {
		try {
			return getGenericDao().findCountByFilter(filter);
		} catch (Exception e) {
			return 0;
		}
	}

	public List<T> findPaginationByFilter(IListFilter filter, int start,
			int pageSize) {
		try {
			return getGenericDao().findPaginationByFilter(filter, start,
					pageSize);
		} catch (Exception e) {
			return new ArrayList<T>();
		}
	}

	@Override
	public List<T> findByAttributes(FilterAttributes attributes) {
		return findByAttributes(attributes, Boolean.FALSE);
	}

	@Override
	public List<T> findByAttributes(FilterAttributes attributes,
			IListFilterPolicy listFilterPolicy) {
		if (listFilterPolicy != null && listFilterPolicy.getAttrs() != null) {
			attributes.add(listFilterPolicy.getAttrs());
		}
		return findByAttributes(attributes, Boolean.FALSE);
	}

	public List<T> findPaginationByAttributes(FilterAttributes attributes,
			int start, int pageSize) {
		return findPaginationByAttributes(attributes, Boolean.FALSE, start,
				pageSize);
	}

	@Override
	public List<T> findByAttributes(FilterAttributes attributes,
			boolean isSupportAopFilter) {
		try {
			IListFilter filter = new GenericListFilter(attributes);
			// 设置是否可以后挂IListFilter
			filter.setCanAppendable(isSupportAopFilter);
			return findByFilter(filter);
		} catch (Exception e) {
			return new ArrayList<T>();
		}
	}

	public List<T> findPaginationByAttributes(FilterAttributes attributes,
			boolean isSupportAopFilter, int start, int pageSize) {
		try {
			IListFilter filter = new GenericListFilter(attributes);
			// 设置是否可以后挂IListFilter
			filter.setCanAppendable(isSupportAopFilter);
			return findPaginationByFilter(filter, start, pageSize);
		} catch (Exception e) {
			return new ArrayList<T>();
		}
	}

	@Override
	public boolean invokeSql(String hql) {
		try {
			getGenericDao().invoke(hql);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public int invokeSql(String[] hqls) {
		int count = 0;
		for (String sql : hqls) {
			boolean b = invokeSql(sql);
			if (b) {
				count++;
			}
		}
		return count;
	}

	@Override
	public List<?> findByHql(String hql) {
		try {
			return getGenericDao().findByHql(hql);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public T findOneByFilter(IListFilter filter) {
		List<T> list = findByFilter(filter);
		Assert.notNull(list);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public T findOneByFilter(FilterAttributes attrs) {
		List<T> list = findByAttributes(attrs);
		Assert.notNull(list);
		if (list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<T> findBySql(String sql) {
		try {
			return getGenericDao().findBySql(sql);
		} catch (Exception e) {
			return new ArrayList<T>();
		}
	}

	@Override
	public boolean deleteAll(Collection<T> collection) {
		try {
			getGenericDao().deleteAll(collection);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	@Override
	public boolean deleteAll(IListFilter filter) {
		try {
			List<T> list = findByFilter(filter);
			getGenericDao().deleteAll(list);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean invokeBySql(String sql) {
		try {
			getGenericDao().invokeBySql(sql);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// ///////////////////////////////////////////////////
	protected GenericDAO<T, Long> genericDao;

	protected GenericDAO<T, Long> getGenericDao() {
		return genericDao;
	}

	@Autowired
	protected void setGenericDao(GenericDAO<T, Long> genericDao) {
		this.genericDao = genericDao;
	}

	@Override
	public void setEntityClass(Class<T> entityClass) {
		this.genericDao.setEntityClass(entityClass);
	}

	@Override
	public String getGenericDaoSpringKey() {
		return null;
	}

}
