package com.etstc.ekits.mask;

import java.util.ArrayList;
import java.util.List;

import com.etstc.ekits.StringUtils;

public final class MaskLexer {

	private MaskLexer() {
		// 单例禁止外部实例化
	}

	public static List<List<MaskToken>> lexer(final String typePattern) {
		List<List<MaskToken>> tokensList = new ArrayList<>();
		if (typePattern == null || typePattern.length() == 0) {
			return tokensList;
		}

		final String pattern = StringUtils.deleteWhitespace(typePattern); // 移除空白字符

		StringBuilder buffer = new StringBuilder();
		boolean bufferHadChar = false; // buffer存在字符
		boolean bufferHadNum = false; // buffer存在数字
		boolean bracketStart = false; // 左括号
		boolean canHadbracket = true; // 是否允许括号
		boolean canHadComma = true; // 是否允许逗号
		boolean canHadPlusOrMinus = true; // 是否允许减号
		boolean canHadColon = true; // 有了符号不能再有冒号

		List<MaskToken> tokens = new ArrayList<>();

		final int len = pattern.length();
		final int lastPos = len - 1;
		for (int i = 0; i < len; i++) {
			char c = pattern.charAt(i);
			switch (c) {
			case ':':
				if (!canHadColon || buffer.length() > 1 || lastPos == i) {
					throw MaskException.lexerError(pattern, i);
				}
				tokens.add(new MaskToken(buffer.toString(), MaskToken.CHAR));
				buffer.delete(0, buffer.length());
				bufferHadChar = false;
				bufferHadNum = false;

				canHadColon = false;
				break;
			case '+':
				if (!canHadPlusOrMinus || buffer.length() > 0) {
					throw MaskException.lexerError(pattern, i);
				}
				tokens.add(MaskToken.TOKEN_PLUS);

				canHadPlusOrMinus = false;
				canHadColon = false;
				break;
			case '-':
				if (!canHadPlusOrMinus || buffer.length() > 0) {
					throw MaskException.lexerError(pattern, i);
				}
				tokens.add(MaskToken.TOKEN_MINUS);

				canHadPlusOrMinus = false;
				canHadColon = false;
				break;
			case '(':
				if (!canHadbracket || bracketStart || buffer.length() > 0) {
					throw MaskException.lexerError(pattern, i);
				}
				canHadPlusOrMinus = false;
				canHadbracket = false;
				bracketStart = true;
				canHadColon = false;
				break;
			case ',':
				if (!canHadComma || !bracketStart || bufferHadChar || !bufferHadNum) {
					throw MaskException.lexerError(pattern, i);
				}

				tokens.add(new MaskToken(buffer.toString(), MaskToken.NUM));
				buffer.delete(0, buffer.length());
				bufferHadNum = false;

				canHadComma = false;
				break;
			case ')':
				if (!bracketStart || bufferHadChar || !bufferHadNum) {
					throw MaskException.lexerError(pattern, i);
				}

				tokens.add(new MaskToken(buffer.toString(), MaskToken.NUM));
				buffer.delete(0, buffer.length());
				bufferHadNum = false;

				bracketStart = false;
				break;
			case ';':
				if (bracketStart || bufferHadChar) {
					throw MaskException.lexerError(pattern, i);
				}

				if (buffer.length() > 0) {
					tokens.add(new MaskToken(buffer.toString(), MaskToken.NUM));
					buffer.delete(0, buffer.length());
					bufferHadNum = false;
				}

				canHadPlusOrMinus = true;
				canHadComma = true;
				canHadbracket = true;
				canHadColon = true;

				tokensList.add(tokens);
				tokens = new ArrayList<>();
				break;
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				if (bufferHadChar && buffer.length() > 0) {
					throw MaskException.lexerError(pattern, i);
				}
				buffer.append(c);
				bufferHadNum = true;

				if (lastPos == i) {
					if (bracketStart || bufferHadChar) {
						throw MaskException.lexerError(pattern, i);
					}
					if (buffer.length() > 0) {
						tokens.add(new MaskToken(buffer.toString(), MaskToken.NUM));
						buffer.delete(0, buffer.length());
						bufferHadNum = false;
					}
				}

				break;
			default:
				if (!canHadColon || buffer.length() > 0 || lastPos == i) {
					throw MaskException.lexerError(pattern, i);
				}
				buffer.append(c);
				bufferHadChar = true;
				break;
			}
		}
		if (tokens.size() > 0) {
			tokensList.add(tokens);
		}
		return tokensList;
	}
}
