package com.etstc.ekits.mask;

import java.util.List;

import org.junit.Test;

public class MaskLexerTest {

	@Test
	public void testLexer() {

		String tx = "$:;2;+3;-3; :0;o:0;o:-3;(1,2);o:(1,2);+(1,2);o:+(2,3)";

		List<List<MaskToken>> list = MaskLexer.lexer(tx);

		for (List<MaskToken> l : list) {
			for (MaskToken x : l) {
				System.out.print(x);
			}
			System.out.println();
		}
	}

	@Test
	public void testMask() {
		System.out.println(MaskUtils.instance.mask("5", "1234567890"));
		System.out.println(MaskUtils.instance.mask("5", "123"));
		System.out.println(MaskUtils.instance.mask("5", "12345"));
		System.out.println(MaskUtils.instance.mask("#:5", "1234567890"));
		System.out.println(MaskUtils.instance.mask("#:5", "123"));
		System.out.println(MaskUtils.instance.mask("#:5", "12345"));
		System.out.println(MaskUtils.instance.mask("+5", "1234567890"));
		System.out.println(MaskUtils.instance.mask("+5", "123"));
		System.out.println(MaskUtils.instance.mask("+5", "12345"));
		System.out.println(MaskUtils.instance.mask("-5", "1234567890"));
		System.out.println(MaskUtils.instance.mask("-5", "123"));
		System.out.println(MaskUtils.instance.mask("-5", "12345"));
	}
}
