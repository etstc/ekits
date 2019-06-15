package com.etstc.ekits.mask;

class MaskException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	MaskException(String pattern, int pos) {
		super("Mask Pattern Lexer Error:{" + pattern + "}@pos=" + pos
				+ "[char='" + pattern.charAt(pos) + "']");
	}

	MaskException() {
		super("Mask Pattern Compiler Error");
	}
}