package org.luis.basic.util;

import java.sql.SQLException;
import java.util.List;

import org.springframework.orm.ibatis.SqlMapClientTemplate;

import com.ibatis.sqlmap.client.SqlMapClient;

/**
 * @Description:
 * @author guoliang.li
 * @since 1.0.0
 */
public final class IbatisBuilder {

	/**
	 * 根据条件查询list
	 * 
	 * @param sqlId
	 * @param obj
	 * @return
	 */
	public static List<?> queryForList(String sqlId, Object obj)
			throws SQLException {
		return getSqlMapClient().queryForList(sqlId, obj);
	}

	/**
	 * 根据id查询
	 * 
	 * @param sqlId
	 * @param id
	 * @return
	 */
	public static Object queryForObject(String sqlId, String id)
			throws SQLException {
		return getSqlMapClient().queryForObject(sqlId, id);
	}

	/**
	 * 插入对象
	 * 
	 * @param sqlId
	 * @param obj
	 *            @
	 */
	public static void doInstert(String sqlId, Object obj) throws SQLException {
		getSqlMapClient().insert(sqlId, obj);
	}

	/**
	 * 修改对象
	 * 
	 * @param sqlId
	 * @param obj
	 */
	public static void doUpdate(String sqlId, Object obj) throws SQLException {
		getSqlMapClient().update(sqlId, obj);
	}

	/**
	 * 根据id删除对象
	 * 
	 * @param sqlId
	 * @param id
	 * @throws SQLException
	 *             @
	 */
	public static void doDeleteById(String sqlId, String id)
			throws SQLException {
		getSqlMapClient().delete(sqlId, id);
	}

	/**
	 * 根据条件删除对象
	 * 
	 * @param sqlId
	 * @param obj
	 * @throws SQLException
	 *             @
	 */
	public static void doDeleteByObj(String sqlId, Object obj)
			throws SQLException {
		getSqlMapClient().delete(sqlId, obj);
	}

	/**
	 * 获取单例SqlMapClient
	 * 
	 * @return
	 */
	public synchronized static SqlMapClientTemplate getSqlMapClient() {
		if (sqlMapClientTemplate == null) {
			try {
				sqlMapClientTemplate = (SqlMapClientTemplate) SpringContextFactory
						.getSpringBean("sqlMapClientTemplate");
			} catch (Exception e) {
				throw new RuntimeException(
						"Something bad happened while building the SqlMapClient instance."
								+ e, e);
			}
		}
		return sqlMapClientTemplate;
	}

	// //////////////////////////////////////////////////////

//	private static SqlMapClient sqlMapClient;

	private static SqlMapClientTemplate sqlMapClientTemplate;

}
