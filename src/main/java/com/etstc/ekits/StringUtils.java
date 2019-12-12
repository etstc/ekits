package com.etstc.ekits;

import java.util.Arrays;
import java.util.Collection;

public interface StringUtils {

	/** 空字符串 */
	static final String EMPTY = "";
	/** 一个空格字符 */
	static final String SPACE = " ";

	/** 指定char+指定长度生成String */
	static String fill(char c, int len) {
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
	static String deleteWhitespace(CharSequence value) {
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

	/** 拼接字符串 */
	static String join(String separator, Collection<CharSequence> args) {
		if (args == null) {
			return null;
		}
		if (separator == null) {
			separator = EMPTY;
		}
		StringBuilder buf = new StringBuilder();
		for (CharSequence t : args) {
			if (t != null) {
				buf.append(t).append(separator);
			}
		}
		buf.deleteCharAt(buf.length() - 1);
		return buf.toString();
	}

	/** 拼接字符串 */
	static String join(String separator, CharSequence... args) {
		if (args == null) {
			return null;
		}
		return join(separator, Arrays.asList(args));
	}
}
