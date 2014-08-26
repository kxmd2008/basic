package org.luis.basic.rest.model;

/**
 * 简单业务请求消息头
 * 
 * @author guoliang.li
 * 
 */
public class SimpleMessageHead implements java.io.Serializable {

	public static final long serialVersionUID = -1803040645536708598L;

	/**
	 * 加密算法代碼：0不加密
	 * 
	 */
	public final static String ENCRPT_NONE = "0";
	/**
	 * 加密算法代碼：1加密
	 * 
	 */
	public final static String ENCRPT_BASE64 = "1";

	/**
	 * 信息加密算法
	 * 
	 */
	private String msg_encrypt = ENCRPT_NONE;
	/**
	 * 请求操作返回代码
	 * 
	 */
	private String rep_code = "";
	/**
	 * 请求操作响应消息
	 * 
	 */
	private String rep_message = "";
	/**
	 * 消息产生时间戳
	 * 
	 */
	private Long timestamp;

	/**
	 * 400 参数错误,必要参数缺失，参数格式错误; 401 没有权限; 500 服务端错误; 200 接口请求成功;
	 * 消息响应状态代码,Client端无用!
	 */
	public final static String REP_OK = "200";
	public final static String REP_CREATED = "201";
	public final static String REP_ACCEPTED = "202";
	public final static String REP_PARAMETER_ERROR = "400";
	public final static String REP_UNAUTHORIZED = "401";
	public final static String REP_FORBIDDEN = "403";
	public final static String REP_NOT_FOUND = "404";
	public final static String REP_METHOD_NOT_ALLOWED = "405";
	public final static String REP_NOT_ACCEPTABLE = "406";
	public final static String REP_REQUEST_TIMEOUT = "408";
	public final static String REP_CONFLICT = "409";
	public final static String REP_SERVICE_ERROR = "500";
	public final static String REP_NOT_IMPLEMENTED = "501";
	public final static String REP_BAD_GATEWAY = "502";
	public final static String REP_SERVICE_UNAVAILABLE = "503";

	/**
	 * 构造函数
	 * 
	 */
	public SimpleMessageHead() {
		this.rep_code = REP_OK;
	}

	/**
	 * 构造函数
	 * 
	 */
	public SimpleMessageHead(String api_code) {
		this(api_code, ENCRPT_NONE);
	}

	/**
	 * 构造函数，指定编码和消息信息
	 * 
	 */
	public SimpleMessageHead(String rep_code,
			String rep_message) {
		this.rep_code = rep_code;
		this.rep_message = rep_message;
		this.timestamp = System.currentTimeMillis();
	}

	public String getMsg_encrypt() {
		return msg_encrypt;
	}

	public void setMsg_encrypt(String msg_encrypt) {
		this.msg_encrypt = msg_encrypt;
	}

	public String getRep_code() {
		return rep_code;
	}

	public void setRep_code(String rep_code) {
		this.rep_code = rep_code;
	}

	public String getRep_message() {
		return rep_message;
	}

	public void setRep_message(String rep_message) {
		this.rep_message = rep_message;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

}
