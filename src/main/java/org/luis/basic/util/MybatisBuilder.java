package org.luis.basic.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

/**
 * @Description:
 * @author guoliang.li
 * @date 2013-1-8 下午1:39:18
 * @since 1.0.0
 */
public final class MybatisBuilder {

	/**
	 * 根据id查询
	 */
	public static Object findOneById(String sqlId, long id) {
		SqlSession session = getSessionFactory().openSession();
		Object obj = null;
		try {
			obj = session.selectOne(sqlId, id);
		} finally {
			session.close();
		}
		return obj;
	}

	/**
	 * 根据查询对象进行查询
	 */
	public static List<?> findListByParam(String sqlId, Object param) {
		SqlSession session = getSessionFactory().openSession();
		List<?> list = new ArrayList<Object>();
		try {
			list = session.selectList(sqlId, param);
		} finally {
			session.close();
		}
		return list;
	}

	/**
	 * 插入单一对象
	 */
	public static boolean insert(String sqlId, Object obj) {
		SqlSession session = getSessionFactory().openSession();
		boolean tof = false;
		try {
			int res = session.insert(sqlId, obj);
			session.commit();
			if (res == 1) {
				tof = true;
			}
		} finally {
			session.close();
		}
		return tof;
	}

	/**
	 * 修改对象
	 */
	public static boolean update(String sqlId, Object obj) {
		SqlSession session = getSessionFactory().openSession();
		boolean tof = false;
		try {
			int res = session.update(sqlId, obj);
			session.commit();
			if (res == 1) {
				tof = true;
			}
		} finally {
			session.close();
		}
		return tof;
	}

	/**
	 * 根据id删除对象
	 * 
	 * @param sqlId
	 * @param id
	 *            @
	 */
	public static boolean deleteById(String sqlId, long id) {
		SqlSession session = getSessionFactory().openSession();
		boolean tof = false;
		try {
			int res = session.delete(sqlId, id);
			session.commit();
			if (res == 1) {
				tof = true;
			}
		} finally {
			session.close();
		}
		return tof;
	}

	/**
	 * 获取单例SqlMapClient
	 * 
	 * @return
	 */
	public synchronized static SqlSessionFactory getSessionFactory() {
		if (sqlSessionFactory == null) {
			sqlSessionFactory = (SqlSessionFactory) SpringContextFactory.getSpringBean("sqlSessionFactory");
		}
		return sqlSessionFactory;
	}

	/**
	 * Mybatis SqlSessionFactory
	 */
	private static SqlSessionFactory sqlSessionFactory;

}
