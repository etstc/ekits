package com.etstc.ekits;

public final class StringUtils {
	private StringUtils() {
		// 禁止外部实例化,调用compile
	}

	/** 空字符串 */
	public static final String EMPTY = "";
	/** 一个空格字符 */
	public static final String SPACE = " ";

	/** 指定char+指定长度生成String */
	public static String fill(char c, int len) {
		if (len < 1) {
			return EMPTY;
		}
		char[] chs = new char[len];
		for (int i = 0; i < len; i++) {
			chs[i] = c;
		}
		return new String(chs, 0, chs.length);
	}

	/** 删除空白字符 */
	public static String deleteWhitespace(CharSequence value) {
		if (value == null) {
			return null;
		} else if (value.length() == 0) {
			return EMPTY;
		} else {
			int len = value.length();
			char[] chs = new char[len];
			int count = 0;
			for (int i = 0; i < len; i++) {
				char ch = value.charAt(i);
				if (Character.isWhitespace(ch)) {
					continue;
				}
				chs[count] = value.charAt(i);
				count++;
			}
			if (count == len && value instanceof String) {
				return value.toString();
			} else {
				return new String(chs, 0, count);
			}
		}
	}
}
