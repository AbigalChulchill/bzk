package com.domi.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.bzk.infrastructure.AESUtils;

@SpringBootTest
public class TestAESUtils {

	@Test
	void testEncryptDencrypt() throws InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, BadPaddingException, IllegalBlockSizeException, IOException {
		String pass = "012345678901234567890123456789zz";
		String org = "qqqqqxxx";
		String etxt = AESUtils.encrypt(pass, org);
		String dtxt= AESUtils.dencrypt(pass, etxt);
		assertEquals(dtxt, org);
	}
	
}
