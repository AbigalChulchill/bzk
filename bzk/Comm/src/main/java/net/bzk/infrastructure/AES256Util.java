package net.bzk.infrastructure;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.util.Base64Utils;

public class AES256Util {

	public static String encrypt(String key,String content ) throws Exception {

		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] result = cipher.doFinal(content.getBytes(StandardCharsets.UTF_8));
//        return parseByte2HexStr(result);
		return Base64.getEncoder().encodeToString(result);
	}

	public static String decrypt(String key,String content ) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
		byte[] bs = Base64Utils.decode(getUTF8Bytes(content));
		Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		return new String(cipher.doFinal(bs), StandardCharsets.UTF_8);
	}

	private static byte[] getUTF8Bytes(String input) {
		return input.getBytes(StandardCharsets.UTF_8);
	}

	public static void main(String[] args) throws Exception {
		String ta = encrypt("123", "1538663015386630");
		System.out.println(ta);
		String da = decrypt(ta, "1538663015386630");
		System.out.println(da);
	}

}