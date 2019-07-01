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
			twoTokens(tokens);
			break;
		case 3:
			// TODO
			break;
		case 4:
			MaskToken t1 = tokens.get(0); // maskChar
			String c = t1.getContent();
			if (c.length() > 0) {
				this.maskChar = c.charAt(0);
			} else {
				this.maskChar = StringUtils.SPACE.charAt(0);
			}

			MaskToken t2 = tokens.get(1); // + or -
			if (MaskToken.TOKEN_MINUS == t2) {
				this.needReverse = true;
			}

			MaskToken t3 = tokens.get(2); // begin
			int begin = Integer.valueOf(t3.getContent());
			MaskToken t4 = tokens.get(3); // length
			int count = Integer.valueOf(t4.getContent());
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

			break;
		default:
			throw MaskException.unsupportedError();
		}
		return this;
	}

	private void oneToken(MaskToken t) {
		if (!t.isNum()) {
			throw MaskException.unsupportedError();
		}
		int count = Integer.valueOf(t.getContent());
		if (count == 0) {
			throw MaskException.maskCharCountError();
		}
		String regexStr = "(.{1," + count + "})(.*)";
		this.hideLen = count;
		this.replacement = PLACEHOLDER + "$2";
		this.regex = Pattern.compile(regexStr);
	}

	private void twoTokens(List<MaskToken> tokens) {
		MaskToken t1 = tokens.get(0);
		MaskToken t2 = tokens.get(1);
		if (!t2.isNum()) {
			throw MaskException.unsupportedError();
		}
		switch (t1.getType()) {
		case MaskToken.CHAR:
			String c = t1.getContent();
			if (c.length() > 0) {
				this.maskChar = c.charAt(0);
			} else {
				this.maskChar = StringUtils.SPACE.charAt(0);
			}
			oneToken(t2);
			break;
		case MaskToken.PLUS:
			oneToken(t2);
			break;
		case MaskToken.MINUS:
			this.needReverse = true;
			oneToken(t2);
			break;
		case MaskToken.NUM:
			// TODO:(1,2)
			break;
		default:
			throw MaskException.unsupportedError();
		}
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