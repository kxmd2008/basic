package org.luis.basic.mongo;

import java.util.ArrayList;
import java.util.List;

import org.luis.basic.util.StringUtils;

import com.mongodb.ServerAddress;

/**
 * Mongo 连接配置参数
 * 
 */
public class MongoConfiguration {

	/**
	 * NGBF缺省MongoDB
	 * 
	 */
	public static MongoConfiguration NGBF = new MongoConfiguration(
			"10.253.46.24", 27017, "ngbfdb", "admin", "123456");

	/**
	 * NGBF缺省MongoDB的测试数据库
	 * 
	 */
	public static MongoConfiguration NGBF_TEST = new MongoConfiguration(
			"10.253.46.24", 27017, "ngbfdb_test", "test", "123456");

	/**
	 * Mongo 数据库IP地址
	 * 
	 */
	private String ip = "";
	/**
	 * Mongo 数据库端口地址
	 * 
	 */
	private int port = -1;
	/**
	 * 登录用户名
	 * 
	 */
	private String username = "";
	/**
	 * 登录密码
	 * 
	 */
	private String password = "";
	/**
	 * 缺省数据库
	 * 
	 */
	private String dbname = "";

	/**
	 * 集群地址
	 * 
	 */
	private String address;
	
	/**
	 * 是否集群
	 * 
	 */
	private boolean cluster;

	/**
	 * 构造函数
	 * 
	 */
	public MongoConfiguration(String ip, int port, String dbname) {
		this(ip, port, dbname, "", "");
	}

	/**
	 * 构造函数,集群，address间用`;`或` `或`,`隔开
	 * 
	 */
	public MongoConfiguration(String address, String dbname,
			String username, String password) {
		this.dbname = dbname;
		this.username = "";
		this.password = "";
		this.address = address;
		this.cluster = Boolean.TRUE;
	}

	public List<ServerAddress> getAddresses() {
		List<ServerAddress> addresses = new ArrayList<ServerAddress>();
		try {
			// 解析集群地址
			String[] addrs = StringUtils.split(this.address, " ");
			for (String s : addrs) {
				String[] temp = StringUtils.split(s, ":");
				ServerAddress address = new ServerAddress(temp[0],
						Integer.parseInt(temp[1]));
				addresses.add(address);
			}
		} catch (Exception e) {
		}
		return addresses;
	}

	/**
	 * 构造函数，增加登录用户名和密码
	 * 
	 */
	public MongoConfiguration(String ip, int port, String dbname,
			String username, String password) {
		this.ip = ip;
		this.port = port;
		this.dbname = dbname;
		this.username = username;
		this.password = password;
		this.cluster = Boolean.FALSE;
	}

	/**
	 * 构造函数
	 * 
	 */
	public MongoConfiguration() {

	}

	// ///////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isCluster() {
		return cluster;
	}

	public void setCluster(boolean cluster) {
		this.cluster = cluster;
		if(!cluster){
			String[] temp = StringUtils.split(address, ":");
			if(temp.length == 2){
				ip = temp[0];
				port = Integer.parseInt(temp[1]);
			}
		}
	}

}
