package com.etstc.ekits.mask;

import java.util.List;

import org.junit.Test;

public class MaskLexerTest {

	@Test
	public void testLexer() {
		String tx = "3;+3;-3;*:3;(3,3);:+3;*:+3;*:-3;*:(3,3);+(3,3);-(3,3);*:+(3,3);*:-(3,3)";
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
		System.out.println(MaskUtils.instance.mask("-2", "1234567890"));
		System.out.println(MaskUtils.instance.mask("-5", "123"));
		System.out.println(MaskUtils.instance.mask("-5", "12345"));
		System.out.println(MaskUtils.instance.mask("*:+(1,10)", "1234567890"));
		System.out.println(MaskUtils.instance.mask("*:+(0,10)", "1234567890"));
		System.out.println(MaskUtils.instance.mask("*:-(1,10)", "1234567890"));
		System.out.println(MaskUtils.instance.mask("*:-(9,3)", "1234567890"));
		System.out.println(MaskUtils.instance.mask("*:-(3,3);:3", "我是中文就是中文不是也是中文"));
		System.out.println(MaskUtils.instance.mask("(3,3)", "12345我是中文就是中文不是也是中文67890"));

		System.out.println(MaskUtils.instance.mask("&:(3,3)", "12345我是中文就是中文不是也是中文67890"));
		System.out.println(MaskUtils.instance.mask("&:(3,3)", "12345我是中文就是中文不是也是中文67890", '#'));
		System.out.println(MaskUtils.instance.mask("&:+(3,3)", "12345我是中文就是中文不是也是中文67890"));
		System.out.println(MaskUtils.instance.mask("&:+(3,3)", "12345我是中文就是中文不是也是中文67890", '#'));
		System.out.println(MaskUtils.instance.mask("&:-(3,3)", "12345我是中文就是中文不是也是中文67890"));
		System.out.println(MaskUtils.instance.mask("&:-(3,3)", "12345我是中文就是中文不是也是中文67890", '#'));
		System.out.println(MaskUtils.instance.mask("&:+3", "12345我是中文就是中文不是也是中文67890"));
		System.out.println(MaskUtils.instance.mask("&:+3", "12345我是中文就是中文不是也是中文67890", '#'));
		System.out.println(MaskUtils.instance.mask("&:-3", "12345我是中文就是中文不是也是中文67890"));
		System.out.println(MaskUtils.instance.mask("&:-3", "12345我是中文就是中文不是也是中文67890", '#'));

	}
}
