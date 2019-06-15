package com.etstc.ekits.mask;

import java.util.ArrayList;
import java.util.List;

public class MaskPattern {

	private List<PatternX> maskPatterns = new ArrayList<PatternX>();

	public MaskPattern(String typePattern) {
		this.compile(typePattern);
	}

	private void compile(String typePattern) {
		List<List<MaskToken>> tokensList = MaskLexer.lexer(typePattern);
		Character maskChar = null;
		for (List<MaskToken> tokens : tokensList) {
			PatternX p = new PatternX(tokens, maskChar);
			if (p != null) {
				maskPatterns.add(p);
			}
		}
	}

	public String mask(CharSequence value) {
		if (value == null) {
			return null;
		} else if (maskPatterns.isEmpty()) {
			return value.toString();
		} else {
			CharSequence result = value;
			for (PatternX regex : maskPatterns) {
				result = regex.mask(result);
			}
			return result.toString();
		}
	}

}
