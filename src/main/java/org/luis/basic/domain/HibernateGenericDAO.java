package org.luis.basic.domain;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

/**
 * DAO实现类，提供数据库访问操作
 * 
 * @author guoliang.li
 * @param <T>
 * @param <ID>
 */
@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class HibernateGenericDAO<T, ID extends Serializable> extends
		HibernateDaoSupport implements GenericDAO<T, ID> {

	private Class<T> entityClass;

	public void setEntityClass(Class<T> entityClass) {
		this.entityClass = entityClass;
	}

	/**
	 * 持久化对象.
	 * 
	 * @param <T>
	 * @param entity
	 * @return
	 */
	@Override
	public T save(T entity) {
		getHibernateTemplate().saveOrUpdate(entity);
		return entity;
	}

	@Override
	public void save(Collection<T> entities) {
		getHibernateTemplate().saveOrUpdateAll(entities);
	}

	/**
	 * 删除对象.
	 * 
	 * @param <T>
	 * @param entity
	 */
	@Override
	public void delete(T entity) {
		getHibernateTemplate().delete(entity);
	}

	/**
	 * 根据ID删除对象
	 * 
	 * @param id
	 */
	@Override
	public void deleteById(ID id) {
		getHibernateTemplate().delete(load(id));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> findBySql(String sql) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		SQLQuery sqlQuery = null;
		List<T> list = null;
		try {
			sqlQuery = session.createSQLQuery(sql);
			sqlQuery.addEntity(entityClass);
			list = sqlQuery.list();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
		return list;

	}

	/**
	 * 根据ID加载对象.
	 * 
	 * @param <T>
	 * @param id
	 * @return
	 */
	@Override
	public T get(ID id) {
		return (T) getHibernateTemplate().get(entityClass, id);
	}

	/**
	 * 根据ID加载对象，并锁定对象.
	 * 
	 * @param <T>
	 * @param id
	 * @return
	 */
	@Override
	public T get(ID id, boolean lock) {
		if (lock) {
			return (T) getHibernateTemplate().get(entityClass, id,
					LockMode.UPGRADE);
		} else {
			return (T) getHibernateTemplate().get(entityClass, id);
		}
	}

	/**
	 * 获取所有对象.
	 * 
	 * @param <T>
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> findAll() {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.createQuery(
						"from " + entityClass.getSimpleName()).list();
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> findByFilter(final IListFilter filter) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = createHql(filter, "select distinct o from ");
				Query query = session.createQuery(hql.toString());
				setQueryParams(query, filter);

				return query.list();
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<T> findPaginationByFilter(final IListFilter filter,
			final int start, final int pageSize) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				String hql = createHql(filter, "select distinct o from ");
				Query query = session.createQuery(hql.toString());
				setQueryParams(query, filter);
				query.setFirstResult(start);
				query.setMaxResults(pageSize);
				return query.list();
			}
		});
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public int findCountByFilter(final IListFilter filter) {
		Long count = (Long) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {
						String hql = createHql(filter,
								"select count(o.id) from ");
						Query query = session.createQuery(hql.toString());
						setQueryParams(query, filter);
						return query.uniqueResult();
					}
				});
		return count.intValue();
	}

	private void setQueryParams(Query query, IListFilter filter) {
		if (filter.getAttrs() != null) {
			setQueryParams(query, filter.getAttrs());
			List<FilterAttributes> andList = filter.getAttrs().getAndAttrs();
			for (FilterAttributes attrs : andList) {
				setQueryParams(query, attrs);
			}
			List<FilterAttributes> orList = filter.getAttrs().getOrAttrs();
			for (FilterAttributes attrs : orList) {
				setQueryParams(query, attrs);
			}
		}
	}

	private void setQueryParams(Query query, FilterAttributes attrs) {
		List<IAttributeNode> list = attrs.getAttrs();
		for (IAttributeNode node : list) {
			FilterAttribute fa = (FilterAttribute) node;
			if (fa.getOp() != FilterAttribute.OP_IN
					&& fa.getOp() != FilterAttribute.OP_NOTIN) {
				query.setParameter(node.getParamName(), node.getValue());
			}
		}
		List<FilterAttributes> andList = attrs.getAndAttrs();
		if (!andList.isEmpty()) {
			for (FilterAttributes fattrs : andList) {
				setQueryParams(query, fattrs);
			}
		}
		List<FilterAttributes> orList = attrs.getOrAttrs();
		if (!orList.isEmpty()) {
			for (FilterAttributes fattrs : orList) {
				setQueryParams(query, fattrs);
			}
		}

	}

	private String createHql(IListFilter filter, String header) {
		StringBuilder hql = new StringBuilder();
		hql.append(header + entityClass.getSimpleName() + " o");
		hql.append(" where 1=1");
		if (filter != null) {
			if (filter.getAttrs() != null) {
				createWhere(hql, filter.getAttrs(), false);
				appendOrAnd(hql, filter.getAttrs());
			}
			if (filter.getOrderAttrs() != null) {
				hql.append(" ").append(filter.getOrderAttrs().toString());
			}
		}
		return hql.toString();
	}

	private void appendOrAnd(StringBuilder hql, FilterAttributes attrs) {
		List<FilterAttributes> andAttrsList = attrs.getAndAttrs();
		if (!andAttrsList.isEmpty()) {
			hql.append(" and (");
			for (FilterAttributes andAttrs : andAttrsList) {
				createWhere(hql, andAttrs, true);
				appendOrAnd(hql, andAttrs);
			}
			hql.append(") ");
		}
		List<FilterAttributes> orAttrsList = attrs.getOrAttrs();
		if (!orAttrsList.isEmpty()) {
			hql.append(" or (");
			for (FilterAttributes orAttrs : orAttrsList) {
				createWhere(hql, orAttrs, true);
				appendOrAnd(hql, orAttrs);
			}
			hql.append(") ");
		}
	}

	private void createWhere(StringBuilder hql, FilterAttributes attrs,
			boolean isInSymbol) {
		List<IAttributeNode> list = attrs.getAttrs();
		for (int i = 0; i < list.size(); i++) {
			FilterAttribute fa = (FilterAttribute) list.get(i);
			if (isInSymbol) {
				if (i != 0) {
					hql.append(" and " + fa.getName());
				} else {
					hql.append(fa.getName());
				}
			} else {
				hql.append(" and " + fa.getName());
			}

			if (fa.getOp() == FilterAttribute.OP_EQUAL) {
				hql.append("=");
			} else if (fa.getOp() == FilterAttribute.OP_STARTWITH) {
				hql.append(" like ");
			} else if (fa.getOp() == FilterAttribute.OP_ENDWITH) {
				hql.append(" like ");
			} else if (fa.getOp() == FilterAttribute.OP_LIKE) {
				hql.append(" like ");
			} else if (fa.getOp() == FilterAttribute.OP_IN) {
				hql.append(" in " + parseListValue(fa.getValue()));
			} else if (fa.getOp() == FilterAttribute.OP_NOTIN) {
				hql.append(" not in " + parseListValue(fa.getValue()));
			} else if (fa.getOp() == FilterAttribute.OP_BIGTHAN) {
				hql.append(">");
			} else if (fa.getOp() == FilterAttribute.OP_NOTLESSTHAN) {
				hql.append(">=");
			} else if (fa.getOp() == FilterAttribute.OP_SMALLTHAN) {
				hql.append("<");
			} else if (fa.getOp() == FilterAttribute.OP_NOTBIGTHAN) {
				hql.append("<=");
			} else if (fa.getOp() == FilterAttribute.OP_NOTEQUAL) {
				hql.append(" <> ");
			} else {
				hql.append("=");
			}
			if (fa.getOp() != FilterAttribute.OP_IN
					&& fa.getOp() != FilterAttribute.OP_NOTIN) {
				hql.append(":").append(fa.getParamName());
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static String parseListValue(Object o) {
		if (o == null) {
			return " ('') ";
		}
		StringBuilder sb = new StringBuilder(" (");
		if (o instanceof List) {
			List<String> list = (List<String>) o;
			if (list.isEmpty()) {
				return " ('') ";
			}
			for (String value : list) {
				sb.append("'").append(value).append("',");
			}
			sb.deleteCharAt(sb.length() - 1);
		} else if (o instanceof String[]) {
			String[] values = (String[]) o;
			if (values.length == 0) {
				return " ('') ";
			}
			for (String value : values) {
				sb.append("'").append(value).append("',");
			}
			sb.deleteCharAt(sb.length() - 1);
		} else if (o instanceof String) {
			sb.append(o.toString());
		}
		return sb.append(") ").toString();
	}

	/**
	 * 根据hql查询.
	 * 
	 * @param <T>
	 * @param hql
	 * @param values
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<T> find(String hql, Object... values) {
		Assert.hasText(hql);
		return getHibernateTemplate().find(hql, values);
	}

	/**
	 * 根据hql查询，并锁定查询对象
	 * 
	 * @param lock
	 * @param hql
	 * @param values
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<T> find(final boolean lock, final String hql,
			final Object... values) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (values != null) {
					for (int i = 0; i < values.length; i++) {
						query.setParameter(i, values[i]);
					}
				}
				if (lock) {
					String alias = getAlias(hql);
					query.setLockMode(alias, LockMode.UPGRADE);
				}
				return query.list();
			}
		});
	}

	private String getAlias(String hql) {
		Assert.hasText(hql);
		String alias = hql.substring(hql.indexOf("from") + 5);
		alias = alias.substring(alias.indexOf(" ") + 1);
		alias = alias.substring(0, alias.indexOf(" "));
		return alias;
	}

	/**
	 * Hibernate load方式加载对象
	 * 
	 * @param <T>
	 * @param id
	 * @return
	 */
	@Override
	public T load(ID id) {
		return (T) getHibernateTemplate().load(entityClass, id);
	}

	@Override
	public T load(ID id, boolean lock) {
		if (lock) {
			return (T) getHibernateTemplate().load(entityClass, id,
					LockMode.UPGRADE);
		} else {
			return (T) getHibernateTemplate().load(entityClass, id);
		}
	}

	/**
	 * Hibernate flush
	 */
	@Override
	public void flush() {
		getHibernateTemplate().flush();
	}

	/**
	 * Hibernate clear
	 */
	@Override
	public void clear() {
		getHibernateTemplate().clear();
	}

	/**
	 * Hibernate evict操作
	 */
	@Override
	public void evict(T entity) {
		getHibernateTemplate().evict(entity);
	}

	@Override
	public void deleteAll() {
		this.getHibernateTemplate().deleteAll(this.findAll());
	}

	@Override
	public void deleteAll(Collection<T> collection) {
		if (collection != null && !collection.isEmpty()) {
			this.getHibernateTemplate().deleteAll(collection);
		}
	}

	@Override
	public void invoke(String hql) {
		this.getHibernateTemplate().bulkUpdate(hql);
	}

	public Object findByHql(String hql) {
		return this.getHibernateTemplate().find(hql);
	}

	public void invokeBySql(String sql) {
		Session session = getSessionFactory().openSession();
		Transaction tx = session.beginTransaction();
		SQLQuery sqlQuery = null;
		try {
			sqlQuery = session.createSQLQuery(sql);
			sqlQuery.addEntity(entityClass);
			sqlQuery.executeUpdate();
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

}
