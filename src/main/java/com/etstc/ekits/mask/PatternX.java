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
	private boolean needReverse = false;

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
			break;
		case 4:
			break;
		default:
			throw new MaskException();
		}
		return this;
	}

	private void oneToken(MaskToken t) {
		if (!t.isNum()) {
			throw new MaskException();
		}
		int count = Integer.valueOf(t.getContent());
		String regexStr = "(.{1," + count + "})(.*)";
		this.hideLen = count;
		this.replacement = PLACEHOLDER + "$2";
		this.regex = Pattern.compile(regexStr);
	}

	private void twoTokens(List<MaskToken> tokens) {
		MaskToken t1 = tokens.get(0);
		MaskToken t2 = tokens.get(1);
		if (!t2.isNum()) {
			throw new MaskException();
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
			break;
		default:
			throw new MaskException();
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
			int tmpHideLen = 0;
			if (this.hideLen > len) {
				tmpHideLen = len;
			} else if (this.keepLen < Integer.MAX_VALUE) {
				tmpHideLen = value.length() - keepLen;
			} else {
				tmpHideLen = this.hideLen;
			}
			tmpReplacement = replacement.replace(PLACEHOLDER,
					StringUtils.fill(maskChar, tmpHideLen));
			if (this.needReverse) {
				String t = regex.matcher(new StringBuilder(value).reverse())
						.replaceAll(tmpReplacement);
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