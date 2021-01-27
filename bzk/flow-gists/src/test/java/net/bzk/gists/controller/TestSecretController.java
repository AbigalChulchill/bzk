package net.bzk.gists.controller;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.ShortBufferException;
import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.Data;
import net.bzk.gists.controller.SecretController.InPlain;
import net.bzk.gists.controller.SecretController.Text;
import net.bzk.infrastructure.JsonUtils;



@SpringBootTest
public class TestSecretController {

	@Inject
	private SecretController controller;
	
	@Test
	void testSecret() throws Exception {
		
		TestC d = new TestC();
		d.setB(10);
		d.setC(true);
		d.setD("cvdf");
		InPlain ep = new InPlain();
		ep.setName(d.getClass().getName());
		ep.setPlain(JsonUtils.toJson(d));
		Text et= controller.encrypt(ep);
		
		InPlain dep = new InPlain();
		dep.setName(d.getClass().getName());
		dep.setPlain(et.getData());
		
		Text det= controller.decrypt(dep);
		System.out.println(JsonUtils.toJson(det));
		
		

	}
	
	@Data
	private static class TestC{
		private String d;
		private int b;
		private boolean c;
	}

}
