package com.etstc.ekits.mask;

import java.util.List;
import java.util.regex.Pattern;

import com.etstc.ekits.StringUtils;

class PatternX {
	private Pattern regex;
	private String replacement;
	private char maskChar = '*';
	private int hideLen = 0;
	private int keepLen = Integer.MAX_VALUE; // 保留长度，用于计算隐位字符个数
	private boolean needReverse = false; // 遇"-"反转字符串执行掩码操作

	private static final String PLACEHOLDER = "{{^M^}}";

	public PatternX(List<MaskToken> tokens) {
		this(tokens, null);
	}

	public PatternX(List<MaskToken> tokens, Character maskChar) {
		this.compile(tokens, maskChar);
	}

	private PatternX compile(List<MaskToken> tokens, Character maskChar) {
		if (tokens == null || tokens.isEmpty()) {
			return null;
		}
		if (maskChar != null) {
			this.maskChar = maskChar;
		}
		switch (tokens.size()) {
		case 1:
			oneToken(tokens.get(0));
			break;
		case 2:
			MaskToken xToken = tokens.get(0);
			MaskToken numToken = tokens.get(1);
			twoTokens(xToken, numToken);
			break;
		case 3:
			MaskToken tX = tokens.get(0);
			MaskToken tY = tokens.get(1);
			MaskToken tZ = tokens.get(2);
			threeTokens(tX, tY, tZ);
			break;
		case 4:
			MaskToken charToken = tokens.get(0); // maskChar
			MaskToken symbolToken = tokens.get(1); // + or -
			MaskToken beginToken = tokens.get(2); // begin
			MaskToken lengthToken = tokens.get(3); // length
			fourTokens(charToken, symbolToken, beginToken, lengthToken);
			break;
		default:
			throw MaskException.unsupportedError();
		}
		return this;
	}

	private void oneToken(MaskToken numToken) {
		if (!numToken.isNum()) {
			throw MaskException.unsupportedError();
		}
		int count = Integer.valueOf(numToken.getContent());
		if (count == 0) {
			throw MaskException.maskCharCountError();
		}
		String regexStr = "(.{1," + count + "})(.*)";
		this.hideLen = count;
		this.replacement = PLACEHOLDER + "$2";
		this.regex = Pattern.compile(regexStr);
	}

	private void twoTokens(MaskToken xToken, MaskToken numToken) {
		if (!numToken.isNum()) {
			throw MaskException.unsupportedError();
		}
		switch (xToken.getType()) {
		case MaskToken.CHAR:
			String c = xToken.getContent();
			if (c.length() > 0) {
				this.maskChar = c.charAt(0);
			} else {
				this.maskChar = StringUtils.SPACE.charAt(0);
			}
			oneToken(numToken);
			break;
		case MaskToken.PLUS:
			oneToken(numToken);
			break;
		case MaskToken.MINUS:
			this.needReverse = true;
			oneToken(numToken);
			break;
		case MaskToken.NUM:
			twoNumberTokens(xToken, numToken);
			break;
		default:
			throw MaskException.unsupportedError();
		}
	}

	/**
	 * 前后保留指定长度
	 * 
	 * @param beginRemToken
	 *            开始保留
	 * @param endRemToken
	 *            结束保留
	 */
	private void twoNumberTokens(MaskToken beginRemToken, MaskToken endRemToken) {
		int beginRem = Integer.valueOf(beginRemToken.getContent());
		int endRem = Integer.valueOf(endRemToken.getContent());
		String regexStr = "(.{" + beginRem + "})(.*)(.{" + endRem + "})";
		int rem = beginRem + endRem;
		this.hideLen = Integer.MAX_VALUE; // 尽可能多的掩码
		if (rem > 0) {
			this.keepLen = rem; // 前后共需保留长度
		}
		this.replacement = "$1" + PLACEHOLDER + "$3";
		this.regex = Pattern.compile(regexStr);
	}

	private void threeTokens(MaskToken xToken, MaskToken yToken, MaskToken numToken) {
		// TODO
	}

	private void fourTokens(MaskToken charToken, MaskToken symbolToken, MaskToken beginToken, MaskToken lengthToken) {
		String c = charToken.getContent();// maskChar
		if (c.length() > 0) {
			this.maskChar = c.charAt(0);
		} else {
			this.maskChar = StringUtils.SPACE.charAt(0);
		}

		if (MaskToken.TOKEN_MINUS == symbolToken) {
			this.needReverse = true;
		}

		int begin = Integer.valueOf(beginToken.getContent());
		int count = Integer.valueOf(lengthToken.getContent());
		if (count == 0) {
			throw MaskException.maskCharCountError();
		}
		String regexStr = "(.{" + begin + "})(.{1," + count + "})(.*)";
		if (begin > 0) {
			this.keepLen = begin;
		}
		this.hideLen = count;
		this.replacement = "$1" + PLACEHOLDER + "$3";
		this.regex = Pattern.compile(regexStr);
	}

	public String mask(CharSequence value) {
		if (value == null) {
			return null;
		} else if (regex == null || replacement == null) {
			return value.toString();
		} else {
			int len = value.length();
			String tmpReplacement = replacement;
			int tmpHideLen = this.hideLen;
			int remLen = len;// 剩余可掩码长度
			if (this.keepLen < Integer.MAX_VALUE) {
				remLen = len - keepLen; // 剩余可掩码长度:字符串总长度-显示长度
			}
			if (this.hideLen > remLen) {
				tmpHideLen = remLen;// 掩码部分大于剩余可掩长度，掩码长度为字符串可掩长度
			}
			tmpReplacement = replacement.replace(PLACEHOLDER, StringUtils.fill(maskChar, tmpHideLen));
			if (this.needReverse) {
				String t = regex.matcher(new StringBuilder(value).reverse()).replaceAll(tmpReplacement);
				return new StringBuilder(t).reverse().toString();
			} else {
				return regex.matcher(value).replaceAll(tmpReplacement);
			}
		}
	}

	public char getMaskChar() {
		return maskChar;
	}

}