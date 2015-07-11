package com.bigcustard.scene2dplus.textarea;

public enum Key {
	Delete('\b'),
	Shift('\u007F'),
	Return('\r'),
	Tab('\t');

	private final char value;
	
	private Key(char value) {
		this.value = value;
	}

	private Key(int value) {
		this((char) value);
	}

	public char asChar() {
		return value;
	}

	public boolean is(char character) {
		return character == value;
	}
}
