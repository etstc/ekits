package com.etstc.ekits.mask;

class MaskToken {
	static final int NONE = 0;
	static final int COLON = 1;
	static final int COMMA = 2;
	static final int SEMICOLON = 3;
	static final int PLUS = 4;
	static final int MINUS = 5;
	static final int LB = 6;
	static final int RB = 7;
	static final int NUM = 8;
	static final int CHAR = 9;

	static final MaskToken TOKEN_COLON = new MaskToken(":", COLON);
	static final MaskToken TOKEN_COMMA = new MaskToken(",", COMMA);
	static final MaskToken TOKEN_SEMICOLON = new MaskToken(";", SEMICOLON);
	static final MaskToken TOKEN_PLUS = new MaskToken("+", PLUS);
	static final MaskToken TOKEN_MINUS = new MaskToken("-", MINUS);
	static final MaskToken TOKEN_LB = new MaskToken("(", LB);
	static final MaskToken TOKEN_RB = new MaskToken(")", RB);

	private int type;
	private String content;

	MaskToken(String content, int type) {
		this.content = content;
		this.type = type;
	}

	boolean isNum() {
		return type == NUM;
	}

	boolean isChar() {
		return type == CHAR;
	}

	int getType() {
		return type;
	}

	void setType(int type) {
		this.type = type;
	}

	String getContent() {
		return content;
	}

	void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "[" + this.content + "," + this.type + "]";
	}
}
