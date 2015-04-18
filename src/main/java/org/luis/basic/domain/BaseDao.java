package org.luis.basic.domain;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

@Repository
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BaseDao<T> {

	public T findOne(String sqlId, Object param) {
		return sqlSessionTemplate.selectOne(sqlId, param);
	}
	
	public List<T> findAll(String sqlId){
		return sqlSessionTemplate.selectList(sqlId);
	}
	
	public List<T> findByAttrs(String sqlId, Object param){
		return sqlSessionTemplate.selectList(sqlId, param);
	}

	/**
	 * 插入单一对象
	 */
	public boolean insert(String sqlId, Object obj) throws RuntimeException {
		boolean tof = false;
		try {
			int res = sqlSessionTemplate.insert(sqlId, obj);
			if (res == 1) {
				tof = true;
			}
		} finally {
		}
		return tof;
	}

	/**
	 * 修改对象
	 */
	public boolean update(String sqlId, Object obj) throws RuntimeException {
		boolean tof = false;
		try {
			int res = sqlSessionTemplate.update(sqlId, obj);
			if (res == 1) {
				tof = true;
			}
		} finally {
		}
		return tof;
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
		boolean tof = false;
		try {
			int res = sqlSessionTemplate.delete(sqlId, param);
			if (res == 1) {
				tof = true;
			}
		} finally {
		}
		return tof;
	}

	@Autowired
	private SqlSessionTemplate sqlSessionTemplate;
}
