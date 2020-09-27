package com.domi.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import net.bzk.infrastructure.PlaceholderUtils;

@SpringBootTest
public class TestPlaceholderUtils {
	
	@Test
	void testGetByTag() {
		final String str = "<tag>apple<tag><b>hello<b><tag>orange<tag><tag>pear<tag>";
		List<String> res = PlaceholderUtils.getByTag("<tag>", str);
		assertEquals(res.size()	, 3);
		assertEquals("apple", res.get(0));
	}

}
