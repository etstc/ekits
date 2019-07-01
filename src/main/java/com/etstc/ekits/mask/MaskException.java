package com.etstc.ekits.mask;

import java.util.Arrays;

class MaskException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	MaskException(String... msgs) {
		super(Arrays.toString(msgs));
	}

	public static final MaskException lexerError(String pattern, int pos) {
		return new MaskException(
				"Mask Pattern Lexer Error:{" + pattern + "}@pos=" + pos + "[char='" + pattern.charAt(pos) + "']");
	}

	public static final MaskException unsupportedError() {
		return new MaskException("Mask Pattern Compiler Error: not supported.");
	}

	public static final MaskException maskCharCountError() {
		return new MaskException("Mask Pattern Compiler Error: mask char count must greater than 0.");
	}
}