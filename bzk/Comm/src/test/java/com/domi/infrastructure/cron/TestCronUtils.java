package com.domi.infrastructure.cron;

import static org.junit.Assert.assertTrue;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestCronUtils {

	@Test
	void testDateAfter() {
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime nextd = ZonedDateTime.now();
		nextd =nextd.plus(1, ChronoUnit.DAYS);
		System.out.println("now:"+now+" next day:"+nextd);
		assertTrue(now.isBefore(nextd));
		assertTrue(nextd.isAfter(now));
	}

}
