package org.luis.basic.security;

import org.luis.basic.util.Encrypt;
import org.springframework.security.authentication.encoding.MessageDigestPasswordEncoder;

public class PasswordEncode extends MessageDigestPasswordEncoder {

	public PasswordEncode(String algorithm) {
		super(algorithm);
	}

	@Override
	public String encodePassword(String rawPass, Object salt) {
		return Encrypt.init(rawPass).md5().genrate();
	}
	
}
