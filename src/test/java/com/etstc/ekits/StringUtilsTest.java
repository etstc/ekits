package com.etstc.ekits;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testFill() {
		String r = StringUtils.fill('E', 3);
		assertTrue("EEE".equals(r));
		String r1 = StringUtils.fill('中', 3);
		assertTrue("中中中".equals(r1));
		String r2 = StringUtils.fill('*', 3);
		assertTrue("***".equals(r2));
	}

	@Test
	public void testDeleteWhitespace() {
		String x = "123red中文";
		String t = "1 2\t3\nr \red中　文";
		String r = StringUtils.deleteWhitespace(t);
		assertTrue(x.equals(r));
	}
}
