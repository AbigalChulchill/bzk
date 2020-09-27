package net.bzk.data.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestConfigRow {
	
	@Test
	void testSpecConvert() {
		ConfigRow cr = new ConfigRow();
		assertEquals(cr, cr);
	}

}
