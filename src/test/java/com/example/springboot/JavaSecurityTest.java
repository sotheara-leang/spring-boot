package com.example.springboot;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.KeyGenerator;

import org.junit.Test;

public class JavaSecurityTest {
	
	@Test 
	public void base64() {
		String data = "hello";
		String encode = Base64.getEncoder().encodeToString( data.getBytes() );
		System.out.println( encode );
		
		byte[] decode = Base64.getDecoder().decode( encode );
		System.out.println( new String(decode) );
	}
	
	@Test
	public void messageDigestMD5() throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("MD5");
		byte[] digest = messageDigest.digest( "Hello".getBytes() );
		System.out.println( Base64.getEncoder().encodeToString( digest ) );
	}
	
	@Test
	public void messageDigestSHA256() throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		byte[] digest = messageDigest.digest( "Hello".getBytes() );
		System.out.println( Base64.getEncoder().encodeToString( digest ) );
	}
	
	@Test
	public void generateKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");

		int keyBitSize = 256;
		SecureRandom secureRandom = new SecureRandom();
		keyGenerator.init(keyBitSize, secureRandom);

		keyGenerator.generateKey();
	}
}
