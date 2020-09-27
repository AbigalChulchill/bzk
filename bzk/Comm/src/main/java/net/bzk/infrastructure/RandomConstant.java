package net.bzk.infrastructure;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomConstant {

	public static final String ABC123_STRING = "qwertyuiopasdfghjklzxcvbnm1234567890QWERTYUIOPASDFGHJKLZXCVBNM";
	
	
	public static String randomUid(int size) {
		String s = RandomStringUtils.random(size, RandomConstant.ABC123_STRING);
		return s;
	}
	
}
