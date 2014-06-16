package org.luis.basic.util;


/**
 * 解密实现
 * @author guoliang.li
 */
public class Decrypt {
	
	/**
	 * 声明要生成的文本
	 */
	private String generateText = "";
	
	/**
	 * 构造函数，输入密文
	 * @param plainText
	 */
	public Decrypt(String secretText) {
		// 缺省密文等于名文
		this.generateText = secretText;
	}
	
	/**
	 * 进行Base64编码
	 * @return
	 */
	public Decrypt base64() {
		this.generateText = CryptUtil.decryptByBase64(generateText);
		return this;
	}
	
	/**
	 * 以charset编码方式进行Base64解密
	 * @return
	 */
	public Decrypt base64(String charset) {
		this.generateText = CryptUtil.decryptByBase64(generateText, charset);
		return this;
	}
	
	/**
	 * 用默认key进行MD5解密
	 */
	public Decrypt md5(){
		this.generateText = CryptUtil.decryptByMD5(generateText);
		return this;
	}
	
	/**
	 * 用key进行MD5解密
	 */
	public Decrypt md5(String key){
		this.generateText = CryptUtil.decryptByMD5(generateText, key);
		return this;
	}
	
	/**
	 * 用默认key进行DES解密
	 */
	public Decrypt des(){
		this.generateText = CryptUtil.decryptByDES(generateText);
		return this;
	}
	
	/**
	 * 用key进行DES解密
	 */
	public Decrypt des(String key){
		this.generateText = CryptUtil.decryptByDES(generateText, key);
		return this;
	}
	
	/**
	 * 简单的key混淆处理
	 * @param key
	 * @return
	 */
	public Decrypt mixup(String key) {
		// 简单的算法处理  generateText = F(generateText + key)
		reverse(key);
		return this;
	}
	
	private void reverse(String key){
		StringBuilder sb = new StringBuilder(generateText);
		generateText = sb.reverse().toString().replace(key, "");
	}
	
	/**
	 * 
	 * @return
	 */
	public String genrate() {
		return this.generateText;
	}
}
