package org.luis.basic.domain.service;

import java.util.List;

import org.luis.basic.domain.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
@Transactional
public class BaseService<T> {
	
	public T findOne(String sqlId, Object param) {
		return baseDao.findOne(sqlId, param);
	}
	
	public List<T> findAll(String sqlId){
		return baseDao.findAll(sqlId);
	}
	
	public List<T> findByAttrs(String sqlId, Object param){
		return baseDao.findByAttrs(sqlId, param);
	}

	/**
	 * 插入单一对象
	 */
	public boolean insert(String sqlId, Object obj) throws RuntimeException {
		return baseDao.insert(sqlId, obj);
	}

	/**
	 * 修改对象
	 */
	public boolean update(String sqlId, Object obj) throws RuntimeException {
		return baseDao.update(sqlId, obj);
	}

	/**
	 * 根据id删除对象
	 * 
	 * @param sqlId
	 * @param param
	 * @throws RuntimeException
	 */
	public boolean delete(String sqlId, Object param)
			throws RuntimeException {
		return baseDao.delete(sqlId, param);
	}

	
	@Autowired
	private BaseDao<T> baseDao;
}
