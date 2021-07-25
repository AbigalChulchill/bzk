package net.bzk.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import net.bzk.infrastructure.tscurve.PeakFinder;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;




@SpringBootTest
public class TestPlaceholderUtils {

//	@Test
//	void testGetByTag() {
//		final String str = "<tag>apple<tag><b>hello<b><tag>orange<tag><tag>pear<tag>";
//		List<String> res = PlaceholderUtils.getByTag("<tag>", str);
//		assertEquals(res.size(), 3);
//		assertEquals("apple", res.get(0));
//	}

	@Test
	void testlistPlaceHolderKeys() {

//		PeakFinder.LastInfo lf = new PeakFinder.LastInfo();

		final String str = "Hi ${name.var}, how are you? I'm ${namevar2}.test ${!namevar2}. ${~namev.ar2}. ${}";
		List<String> res = PlaceholderUtils.listStringSubstitutorKeys(str);
		System.out.println(res);
		assertEquals(res.size(), 4);

	}

}
