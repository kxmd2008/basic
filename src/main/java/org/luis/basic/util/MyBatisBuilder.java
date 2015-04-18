package org.luis.basic.util;

import java.util.ArrayList;
import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;


/**
 * 
 * @author Guoliang.Li
 */
public final class MyBatisBuilder {


	/**
	 * 根据id查询
	 */
	public static <T> T findOneById(String sqlId, Object param) {
		T obj = null;
		try {
			obj = sqlSessionTemplate.<T> selectOne(sqlId, param);
		} finally {
		}
		return obj;
	}
	
	/**
	 * 根据查询对象进行查询
	 */
	public static <T> List<T> findListByParam(String sqlId, Object param) {
		List<T> list = new ArrayList<T>();
		try {
			list = sqlSessionTemplate.selectList(sqlId, param);
		} finally {
		}
		return list;
	}

	/**
	 * 插入单一对象
	 */
	public static boolean insert(String sqlId, Object obj)
			throws RuntimeException {
		boolean tof = false;
		try {
			int res =sqlSessionTemplate.insert(sqlId, obj);
			sqlSessionTemplate.commit();
			if(res==1){
				tof = true;
			}
		} finally {
		}
		return tof;
	}

	/**
	 * 修改对象
	 */
	public static boolean update(String sqlId, Object obj)
			throws RuntimeException {
		boolean tof = false;
		try {
			int res =sqlSessionTemplate.update(sqlId, obj);
			sqlSessionTemplate.commit();
			if(res==1){
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
	public static boolean deleteById(String sqlId, Object param)
			throws RuntimeException {
		boolean tof = false;
		try {
			int res =sqlSessionTemplate.delete(sqlId, param);
			sqlSessionTemplate.commit();
			if(res==1){
				tof = true;
			}
		} finally {
		}
		return tof;
	}

	
	/**
	 * 获取单例SqlMapClient
	 * 
	 * @return
	 */
	public synchronized static SqlSessionTemplate getSessionFactory() {
		if (sqlSessionTemplate == null) {
			try {
				// TODO http://mybatis.github.io/
				// TODO http://legend2011.blog.51cto.com/3018495/926219
				// TODO Jar包内的类访问外部的配置文件
				sqlSessionTemplate = SpringContextFactory.getSpringBean(SqlSessionTemplate.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return sqlSessionTemplate;
	}

	public static void main(String[] args) {
		
//		DemoBean bean = (DemoBean) MybatisBuilder.findOneById("ns.DemoBean.findOneById", 6l);
//		System.out.println(bean.getName());
		
//		List<?> list = MyBatisBuilder.findListByParam("ns.DemoBean.findListByParam", new DemoBean("a1"));
//		System.out.println(list.size());
//		
//		boolean b1 = MyBatisBuilder.insert("ns.DemoBean.insert", new DemoBean("a1"));
//		System.out.println(b1);
//		
//		boolean b2 = MyBatisBuilder.deleteById("ns.DemoBean.deleteById", 6);
//		System.out.println(b2);
	}


	/**
	 * Mybatis SqlSessionFactory
	 */
//	private static SqlSessionFactory sqlSessionFactory;

	private static SqlSessionTemplate sqlSessionTemplate;

}

