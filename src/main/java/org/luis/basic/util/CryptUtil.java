package org.luis.basic.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.mail.internet.MimeUtility;

import org.apache.log4j.Logger;

/**
 * 加解密工具类
 * 
 * @author guoliang.li
 */
public final class CryptUtil {
	private static final Logger logger = Logger.getLogger(CryptUtil.class);
	public static final String CHARSET_GB2312 = "GB2312";
	public static final String CHARSET_UTF8 = "UTF-8";
	public static final String ALGORITHM_BASE64 = "base64";
	public static final String ALGORITHM_MD5 = "MD5";
	public static final String ALGORITHM_DES = "DES";
	public static final String ALGORITHM_PBEWITHMD5ANDDES = "PBEWithMD5AndDES";
	/** 值越大，加密越深 */
	private static final int ITERATIONS = 20;
	
	public static final String DEFAULT_KEY = "QAZWSXedcrfv";

	public static byte[] encryptByBase64(byte[] byteData) {
		ByteArrayOutputStream baos = null;
		OutputStream b64os = null;
		byte[] res = null;
		try {
			baos = new ByteArrayOutputStream();
			b64os = MimeUtility.encode(baos, "base64");
			b64os.write(byteData);
			b64os.close();
			return baos.toByteArray();
		} catch (Exception e) {
			e.printStackTrace();
			return res;
		} finally {
			try {
				if (baos != null) {
					baos.close();
					baos = null;
				}
			} catch (Exception e) {
			}
			try {
				if (b64os != null) {
					b64os.close();
					b64os = null;
				}
			} catch (Exception e) {
			}
		}
	}

	public static byte[] decryptByBase64(byte[] data) throws Exception {
		ByteArrayInputStream bais = null;
		InputStream b64is = null;
		try {
			bais = new ByteArrayInputStream(data);
			b64is = MimeUtility.decode(bais, ALGORITHM_BASE64);
			byte[] tmp = new byte[data.length];
			int n = b64is.read(tmp);
			byte[] res = new byte[n];
			System.arraycopy(tmp, 0, res, 0, n);
			return res;
		} catch (Exception e) {
			throw new Exception(e);
		} finally {
			try {
				if (bais != null) {
					bais.close();
					bais = null;
				}
			} catch (Exception e) {
			}
			try {
				if (b64is != null) {
					b64is.close();
					b64is = null;
				}
			} catch (Exception e) {
			}
		}
	}

	/**
	 * base64加密，默认字符集：GB2312
	 * 
	 * @author guoliang.li
	 * @date 2013-7-26
	 * @param strInput
	 * @return
	 */
	public static String encryptByBase64(String strInput) {
		if (strInput == null)
			return null;
		return encryptByBase64(strInput, CHARSET_GB2312);
	}

	/**
	 * base64解密，默认字符集：GB2312
	 * 
	 * @author guoliang.li
	 * @date 2013-7-26
	 * @param strInput
	 * @return
	 */
	public static String decryptByBase64(String strInput) {
		if (strInput == null)
			return null;
		return decryptByBase64(strInput, CHARSET_GB2312);
	}

	/**
	 * base64加密
	 * 
	 * @author guoliang.li
	 * @date 2013-7-26
	 * @param strInput
	 *            待加密string
	 * @param charSet
	 *            字符集
	 * @return
	 */
	public static String encryptByBase64(String strInput, String charSet) {
		if (strInput == null)
			return null;
		String strOutput = null;
		byte byteData[] = new byte[strInput.length()];
		try {
			byteData = strInput.getBytes(charSet);
			strOutput = new String(encryptByBase64(byteData), charSet);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
		return strOutput;
	}

	/**
	 * base64解密
	 * 
	 * @date 2013-7-26
	 * @param strInput
	 *            待加密string
	 * @param charSet
	 *            字符集
	 * @return
	 */
	public static String decryptByBase64(String strInput, String charSet) {
		if (strInput == null)
			return null;
		String strOutput = null;
		byte byteData[] = new byte[strInput.length()];
		try {
			byteData = strInput.getBytes(charSet);
			strOutput = new String(decryptByBase64(byteData), charSet);
		} catch (Exception e) {
			return null;
		}
		return strOutput;
	}
	
	public static String encryptByMD5(String plainText) {
		return encryptByMD5(plainText, DEFAULT_KEY);
	}

	/**
	 * MD5加密
	 * @author guoliang.li
	 * @date 2013-7-26
	 * @param key
	 * @param plainText 待加密字符串
	 * @return
	 */
	public static String encryptByMD5(String plainText, String key) {
		try {
			byte[] salt = new byte[8];
			MessageDigest md = MessageDigest.getInstance(ALGORITHM_MD5);
			md.update(key.getBytes());
			byte[] digest = md.digest();
			for (int i = 0; i < 8; i++) {
				salt[i] = digest[i];
			}
			// 使用的是PBEWithMD5AndDES加密
			PBEKeySpec pbeKeySpec = new PBEKeySpec(key.toCharArray());
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance(ALGORITHM_PBEWITHMD5ANDDES);
			// 生成key
			SecretKey skey = keyFactory.generateSecret(pbeKeySpec);
			PBEParameterSpec paramSpec = new PBEParameterSpec(salt, ITERATIONS);
			// 得到加密/解密器
			Cipher cipher = Cipher.getInstance(ALGORITHM_PBEWITHMD5ANDDES);
			// 用指定的密钥和模式初始化Cipher对象
			cipher.init(Cipher.ENCRYPT_MODE, skey, paramSpec);
			// 对要加密的内容进行编码处理
			byte[] cipherText = cipher.doFinal(plainText.getBytes());
			String saltString = new String(encryptByBase64(salt));
			String ciphertextString = new String(encryptByBase64(cipherText));

			// Weblogic容器下会有无辜增加\n，去掉 @since 1.3.1.0
			saltString = saltString.replaceAll("\n", "");
			saltString = saltString.replaceAll("\r", "");
			ciphertextString = ciphertextString.replaceAll("\n", "");
			ciphertextString = ciphertextString.replaceAll("\r", "");
			return saltString + ciphertextString;
		} catch (Exception e) {
			e.printStackTrace();
			return plainText;
		}
	}
	
	public static String decryptByMD5(String encryptTxt) {
		return decryptByMD5(encryptTxt, DEFAULT_KEY);
	}

	/**
	 * MD5解密
	 * @param key
	 * @param encryptTxt 待解密字符串
	 * @return
	 */
	public static String decryptByMD5(String encryptTxt, String key) {
		// base64解码后的盐的长度，加密后再经BASE64编码后盐的长度为8，BASE64解码后盐的长度为12
		int saltLength = 12;
		try {
			String salt = encryptTxt.substring(0, saltLength);
			String ciphertext = encryptTxt.substring(saltLength,
					encryptTxt.length());
			byte[] saltarray = decryptByBase64(salt.getBytes());
			byte[] ciphertextArray = decryptByBase64(ciphertext.getBytes());
			// 使用的是PBEWithMD5AndDES加密
			PBEKeySpec keySpec = new PBEKeySpec(key.toCharArray());
			SecretKeyFactory keyFactory = SecretKeyFactory
					.getInstance(ALGORITHM_PBEWITHMD5ANDDES);
			// 生成key
			SecretKey skey = keyFactory.generateSecret(keySpec);
			PBEParameterSpec paramSpec = new PBEParameterSpec(saltarray,
					ITERATIONS);
			// 得到加密/解密器
			Cipher cipher = Cipher.getInstance(ALGORITHM_PBEWITHMD5ANDDES);
			// 用指定的密钥和模式初始化Cipher对象
			cipher.init(Cipher.DECRYPT_MODE, skey, paramSpec);
			// 对要解密的内容进行编码处理
			byte[] plaintextArray = cipher.doFinal(ciphertextArray);
			return new String(plaintextArray);
		} catch (Exception e) {
			e.printStackTrace();
			return encryptTxt;
		}
	}
	
	/**
     * 据DES算法加密
     * @param src 数据源
     * @param key 密钥，长度必须是8的倍数
     * @return    返回加密后的数据
     * @throws Exception
     */
    public static byte[] encrypt(byte[] src, byte[] key) throws RuntimeException {
        //      DES算法要求有一个可信任的随机数源
        try{
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成加密操作
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            // 用密匙初始化Cipher对象  
            cipher.init(Cipher.ENCRYPT_MODE, securekey, sr);
            // 现在，获取数据并加密
            // 正式执行加密操作
            return cipher.doFinal(src);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    /**
     * 据DES算法解密
     * @param src   数据源
     * @param key   密钥，长度必须是8的倍数
     * @return      返回解密后的原始数据
     * @throws Exception
     */
    public static byte[] decrypt(byte[] src, byte[] key) throws RuntimeException {
        try{
            //      DES算法要求有一个可信任的随机数源
            SecureRandom sr = new SecureRandom();
            // 从原始密匙数据创建一个DESKeySpec对象
            DESKeySpec dks = new DESKeySpec(key);
            // 创建一个密匙工厂，然后用它把DESKeySpec对象转换成
            // 一个SecretKey对象
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM_DES);
            SecretKey securekey = keyFactory.generateSecret(dks);
            // Cipher对象实际完成解密操作
            Cipher cipher = Cipher.getInstance(ALGORITHM_DES);
            // 用密匙初始化Cipher对象
            cipher.init(Cipher.DECRYPT_MODE, securekey, sr);
            // 现在，获取数据并解密
            // 正式执行解密操作
            return cipher.doFinal(src);
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    /**
     * 据DES算法加密数据
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptByDES(String data){
    	return encryptByDES(data, DEFAULT_KEY);
    }
	
	/**
     * 据DES算法加密数据
     * @param data
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public static String encryptByDES(String data, String key){
        if(data!=null) {
	        try {
	            return byte2hex(encrypt(data.getBytes(),key.getBytes()));
	        }catch(Exception e) {
	            throw new RuntimeException(e);
	        }
        }
        return null;
    }
    
    /**
     * 据DES算法解密数据
     * @param data
     * @return
     * @throws Exception
     */
    public static String decryptByDES(String data){
    	return decryptByDES(data, DEFAULT_KEY);
    }
	
    /**
     * 据DES算法解密数据
     * @param data
     * @param key 密钥
     * @return
     * @throws Exception
     */
    public static String decryptByDES(String data, String key){
    	try {
    		return new String(decrypt(hex2byte(data.getBytes()),key.getBytes()));
		} catch (Exception e) {
			return null;
		}
    }
	
	/**
     * 十六进制转字符串
     * @param b
     * @return
     */
	public static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b!=null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

	/**
	 * 十六进制转byte
	 * @author guoliang.li
	 * @date 2013-7-26
	 * @param b
	 * @return
	 */
    public static byte[] hex2byte(byte[] b) {
        if((b.length%2)!=0)
            throw new IllegalArgumentException();
        byte[] b2 = new byte[b.length/2];
        for (int n = 0; n < b.length; n+=2) {
            String item = new String(b,n,2);
            b2[n/2] = (byte)Integer.parseInt(item,16);
        }
        return b2;
    }
    
    //////////////////////////////////////////////////////////////////////////
    ///////////////                     数字签名                                 //////////////
    //////////////////////////////////////////////////////////////////////////
    /**
	 * DSA算法
	 */
	public static final String ALG_DSA = "DSA";
	// /**
	// * RSA算法
	// */
	// public static final String ALG_RSA = "RSA";
	/**
	 * SHA1withRSA算法
	 */
	public static final String ALG_SHA1 = "SHA1withRSA";
	/**
	 * 公匙文件名
	 */
	public static final String FILE_PUBLIC_KEY = "public.data";
	/**
	 * 私匙文件名
	 */
	public static final String FILE_PRIVATE_KEY = "private.data";
	/**
	 * KeyStore类型,Java支持的缺省类型是JKS
	 */
	public static final String KEY_STORE = "JKS";
	/**
	 * X.509证书
	 */
	public static final String X509 = "X.509";
	
	

	/**
	 * 采用SHA1withRSA算法对内容数据签名
	 */
	public static String signWithSha1(String content, PrivateKey privateKey) {
		try {
			Signature signature = Signature.getInstance(ALG_SHA1);
			signature.initSign(privateKey);
			signature.update(content.getBytes());
			byte[] sign = signature.sign();
			
			return jodd.util.Base64.encodeToString(sign);
			
		} catch (Exception e) {
			logger.error("数据签名失败" + content + "\t" + ALG_SHA1, e);
			return null;
		}
	}

	/**
	 * 采用SHA1withRSA算法对内容数据验签
	 */
	public static boolean verifyWithSha1(String content, String sign,
			PublicKey publicKey) {
		try {
			Signature signature = Signature.getInstance(ALG_SHA1);
			signature.initVerify(publicKey);
			signature.update(content.getBytes());
			return signature.verify(jodd.util.Base64.decode(sign));
		} catch (Exception e) {
			logger.error("验证数据失败!", e);
		}
		return false;
	}

	/**
	 * 从keyStore文件中获取私钥对象
	 */
	public static PrivateKey getPrivateKeyFromKeyStore(String keyStorePath,
			String keyStorePassword, String privateKeyAlias,
			String privateKeyPassword) {
		try {
			KeyStore ks = getKeyStore(keyStorePath, keyStorePassword);
			PrivateKey key = (PrivateKey) ks.getKey(privateKeyAlias,
					privateKeyPassword.toCharArray());
			return key;
		} catch (Exception e) {
			logger.error("从keyStore文件中获取私钥对象失败:\t" + keyStorePath, e);
			return null;
		}

	}
	
	/**
	 * 从keyStore文件中获取私钥对象
	 */
	public static PrivateKey getPrivateKeyFromKeyStore(InputStream isKeyStore,
			String keyStorePassword, String privateKeyAlias,
			String privateKeyPassword) {
		try {
			KeyStore ks = getKeyStore(isKeyStore, keyStorePassword);
			PrivateKey key = (PrivateKey) ks.getKey(privateKeyAlias,
					privateKeyPassword.toCharArray());
			return key;
		} catch (Exception e) {
			logger.error("从keyStore文件中获取私钥对象失败:\t" , e);
			return null;
		}

	}

	/**
	 * 从KeyStore文件中获取公钥
	 */
	public static PublicKey getPublicKeyFromKeyStore(String keyStorePath,
			String keyStorePassword, String keyStoreAlias) {
		try {
			KeyStore ks = getKeyStore(keyStorePath, keyStorePassword);
				Certificate certificate = ks.getCertificate(keyStoreAlias);
				PublicKey key = certificate.getPublicKey();
			return key;
		} catch (Exception e) {
			logger.error("从KeyStore文件中获取公钥失败:\t" + keyStorePath, e);
			return null;
		}
	}

	/**
	 * 从KeyStore文件中获取公钥
	 */
	public static PublicKey getPublicKeyFromKeyStore(InputStream isKeyStore,
			String keyStorePassword, String keyStoreAlias) {
		try {
			KeyStore ks = getKeyStore(isKeyStore, keyStorePassword);
			Certificate certificate = ks.getCertificate(keyStoreAlias);
			PublicKey key = certificate.getPublicKey();
			return key;
		} catch (Exception e) {
			logger.error("从KeyStore文件中获取公钥失败:\t" , e);
			return null;
		}
	}
	
	/**
	 * 通过证书文件获得公钥
	 */
	public static PublicKey getPublicKeyFromCertificate(String certificatePath) {
		try {
			Certificate certificate = getCertificate(certificatePath);
			return certificate.getPublicKey();
		} catch (Exception e) {
			logger.error("通过证书文件获得公钥失败:\t" + certificatePath, e);
			return null;
		}

	}

	/**
	 * 从KeyStore获得证书对象
	 */
	public static Certificate getCertificate(InputStream isKeyStore,
			String keyStorePassword, String keyStoreAlias) {
		try {
			KeyStore ks = getKeyStore(isKeyStore, keyStorePassword);
			Certificate certificate = ks.getCertificate(keyStoreAlias);
			return certificate;
		} catch (Exception e) {
			logger.error("从KeyStore获得证书失败:\t" , e);
			return null;
		}
	}
	
	/**
	 * 从KeyStore获得证书对象
	 */
	public static Certificate getCertificate(String keyStorePath,
			String keyStorePassword, String keyStoreAlias) {
		try {
			KeyStore ks = getKeyStore(keyStorePath, keyStorePassword);
			Certificate certificate = ks.getCertificate(keyStoreAlias);
			return certificate;
		} catch (Exception e) {
			logger.error("获得证书失败:\t" + keyStorePath, e);
			return null;
		}
	}

	/**
	 * 验证证书文件是否有效
	 */
	public static boolean verifyCertificate(Date date, String certificatePath) {
		boolean tof = false;
		try {
			// 取得证书
			Certificate certificate = getCertificate(certificatePath);
			// 验证证书是否过期或无效
			X509Certificate x509Certificate = (X509Certificate) certificate;
			x509Certificate.checkValidity(date);
			tof = true;
		} catch (Exception e) {
			logger.error("证书文件无效", e);
		}
		return tof;
	}

	/**
	 * 从证书.cert文件获得证书对象
	 */
	public static Certificate getCertificate(String certificatePath) {
		try {
			CertificateFactory certificateFactory = CertificateFactory
					.getInstance(X509);
			FileInputStream in = new FileInputStream(certificatePath);
			Certificate certificate = certificateFactory
					.generateCertificate(in);

			in.close();
			return certificate;
		} catch (Exception e) {
			logger.error("获得证书失败", e);
			return null;
		}
	}
	
	

	/**
	 * 获得KeyStore对象，需指定keyStore文件路径和密码
	 */
	private static KeyStore getKeyStore(String keyStorePath, String password) {
		try {
			FileInputStream is = new FileInputStream(keyStorePath);
			KeyStore ks = KeyStore.getInstance(KEY_STORE);
			ks.load(is, password.toCharArray());
			is.close();
			return ks;
		} catch (Exception e) {
			logger.error("获得KeyStore失败", e);
			return null;
		}
	}
	
	public static KeyStore getKeyInfo(String pfxkeyfile, String keypwd, String type)
	  {
	    try
	    {
	      KeyStore ks = null;
	      if ("JKS".equals(type)) {
	        ks = KeyStore.getInstance(type);
	      } else if ("PKCS12".equals(type)) {  
	        ks = KeyStore.getInstance(type);
	      }
	      FileInputStream fis = new FileInputStream(pfxkeyfile);
	      char[] nPassword = (char[])null;
	      nPassword = ((keypwd == null) || ("".equals(keypwd.trim()))) ? null : 
	        keypwd.toCharArray();
	      ks.load(fis, nPassword);
	      fis.close();
	      return ks;
	    }catch( Exception e ) {
	    	e.printStackTrace();
	    }
	    return null;
	  }
	
	/**
	 * 获得KeyStore对象，需指定keyStore文件路径和密码
	 */
	private static KeyStore getKeyStore(InputStream isKeyStorePath, String password) {
		try {
			KeyStore ks = KeyStore.getInstance(KEY_STORE);
			ks.load(isKeyStorePath, password.toCharArray());
			isKeyStorePath.close();
			return ks;
		} catch (Exception e) {
			logger.error("获得KeyStore失败", e);
			return null;
		}
	}

	/**
	 * 依据算法产生钥匙对文件private.data和public.data到指定的文件夹内 支持算法为DSA
	 */
	public static void generateKeyPair(String filedir, String algorithm)
			throws Exception {
		KeyPairGenerator keyGen = KeyPairGenerator.getInstance(algorithm);
		keyGen.initialize(1024, new SecureRandom());// 设定随机产生器
		// 生成密钥公钥pubkey和私钥prikey
		KeyPair pair = keyGen.generateKeyPair();
		PublicKey pk = pair.getPublic();
		PrivateKey sk = pair.getPrivate();
		// 公钥存文件
		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
				filedir + "/" + FILE_PUBLIC_KEY));
		oos.writeObject(pk);
		oos.close();
		// 私钥存文件
		oos = new ObjectOutputStream(new FileOutputStream(filedir + "/"
				+ FILE_PRIVATE_KEY));
		oos.writeObject(sk);
		oos.close();
	}

	/**
	 * 从公钥文件中获得公匙对象
	 */
	public static PublicKey getPublicKeyFromFile(String filePath) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					filePath));
			PublicKey pk = (PublicKey) ois.readObject();
			ois.close();
			return pk;
		} catch (Exception e) {
			logger.error("数据公钥失败" + filePath, e);
		}
		return null;
	}

	/**
	 * 从文件获得私匙对象
	 */
	public static PrivateKey getPrivateKeyFromFile(String filePath) {
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
					filePath));
			PrivateKey pk = (PrivateKey) ois.readObject();
			ois.close();
			return pk;
		} catch (Exception e) {
			logger.error("数据私钥失败" + filePath, e);
		}
		return null;
	}

	/**
	 * 使用私钥对数据签名
	 */
	public static String signature(String content, PrivateKey privateKey,
			String algorithm) {
		try {
			Signature dsa = Signature.getInstance(algorithm);
			dsa.initSign(privateKey);
			ByteArrayOutputStream streamRaw = new ByteArrayOutputStream();
			DataOutputStream streamSig = new DataOutputStream(streamRaw);
			streamSig.writeUTF(content);
			dsa.update(streamRaw.toByteArray());
			byte[] signature = dsa.sign();
			return encodeHex(signature);
		} catch (Exception e) {
			logger.error("数据签名失败" + content + "\t" + algorithm, e);
			// throw new NgbfRuntimeException("数据签名失败!");
			return null;
		}
	}

	/**
	 * 使用公钥验证数据
	 */
	public static boolean verify(String content, String signature,
			PublicKey publicKey, String algorithm) {
		try {
			Signature dsa = Signature.getInstance(algorithm);
			dsa.initVerify(publicKey);
			ByteArrayOutputStream streamRaw = new ByteArrayOutputStream();
			DataOutputStream streamSig = new DataOutputStream(streamRaw);
			streamSig.writeUTF(content);
			dsa.update(streamRaw.toByteArray());
			return dsa.verify(decodeHex(signature));
		} catch (Exception e) {
			logger.error("验证数据失败" + content + "\t" + signature, e);
			return false;
		}
	}

	// /////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////
	// /////////////////////////////////////////////////////////////
	/**
	 * byte[]转为16进制String
	 * 
	 * @author Guoliang.Li
	 * @date 2013-1-17
	 * @param bytes
	 * @return
	 */
	private static final String encodeHex(byte bytes[]) {
		StringBuffer buf = new StringBuffer(bytes.length * 2);
		for (int i = 0; i < bytes.length; i++) {
			if ((bytes[i] & 0xff) < 16)
				buf.append("0");
			buf.append(Long.toString(bytes[i] & 0xff, 16));
		}

		return buf.toString();
	}

	/**
	 * 16进制String转为byte[]
	 * 
	 * @author Guoliang.Li
	 * @date 2013-1-17
	 * @param hex
	 * @return
	 */
	private static final byte[] decodeHex(String hex) {
		char chars[] = hex.toCharArray();
		byte bytes[] = new byte[chars.length / 2];
		int byteCount = 0;
		for (int i = 0; i < chars.length; i += 2) {
			int newByte = 0;
			newByte |= charToByte(chars[i]);
			newByte <<= 4;
			newByte |= charToByte(chars[i + 1]);
			bytes[byteCount] = (byte) newByte;
			byteCount++;
		}

		return bytes;
	}

	/**
	 * char转为byte
	 * 
	 * @author Guoliang.Li
	 * @date 2013-1-17
	 * @param c
	 * @return
	 */
	private static byte charToByte(char c) {
		return (byte) "0123456789abcdef".indexOf(c);
	}
	
	public static void main(String[] args) {
		String plain = "This is a secret...";
		String secret = CryptUtil.encryptByBase64(plain);
		System.out.println(secret);
		
	}
}
