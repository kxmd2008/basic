package org.luis.basic.util;


/**
 * 加密实现
 * 
 * @description
 * @author guoliang.li
 * @date 2013-8-13 下午2:15:24
 * @since 1.6.4.0
 */

public class Encrypt {

	public static void main(String[] args) {
		String plain = "123456";
		Encrypt encrypt = Encrypt.init(plain).md5();
		System.out.println(encrypt.genrate());
		String secret = encrypt.base64().md5().des().genrate();
		
		System.out.println("ENCRYPT:\t" + secret);
		String plain2 = new Decrypt(secret).des().md5().base64().mixup("12345").genrate();
		System.out.println("DECRYPT:\t" + plain2);
	}

	/**
	 * 声明要生成的文本
	 */
	private String generateText = "";
	
	/**
	 * 构造函数，输入明文
	 * @param plainText
	 */
	public Encrypt(String plainText) {
		// 缺省明文等于密文
		this.generateText = plainText;
	}
	
	/**
	 * 进行Base64编码
	 * @return
	 */
	public Encrypt base64() {
		this.generateText = CryptUtil.encryptByBase64(generateText);
		return this;
	}
	
	/**
	 * 以charset编码方式进行Base64加密
	 * @return
	 */
	public Encrypt base64(String charset) {
		this.generateText = CryptUtil.encryptByBase64(generateText, charset);
		return this;
	}
	
	/**
	 * 用默认key进行MD5加密
	 */
	public Encrypt md5() {
		this.generateText = CryptUtil.encryptByMD5(generateText);
		return this;
	}
	
	/**
	 * 用key进行MD5加密
	 */
	public Encrypt md5(String key) {
		this.generateText = CryptUtil.encryptByMD5(generateText, key);
		return this;
	}
	
	/**
	 * 用默认key进行DES加密
	 */
	public Encrypt des() {
		this.generateText = CryptUtil.encryptByDES(generateText);
		return this;
	}
	
	/**
	 * 用key进行DES加密
	 */
	public Encrypt des(String key) {
		this.generateText = CryptUtil.encryptByDES(generateText, key);
		return this;
	}
	
	/**
	 * 简单的key混淆处理
	 * @param key
	 * @return
	 */
	public Encrypt mixup(String key) {
		// 简单的算法处理  generateText = G(generateText + key)
		reverse(key);
		return this;
	}
	
	private void reverse(String key){
		StringBuilder sb = new StringBuilder(generateText + key);
		generateText = sb.reverse().toString();
	}
	
	/**
	 * 
	 * @return
	 */
	public String genrate() {
		return this.generateText;
	}
	
	public static Encrypt init(String txt){
		return new Encrypt(txt);
	}

}
