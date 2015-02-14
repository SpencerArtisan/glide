package com.mygdx.game.code;


public class SyntaxPart {
	private String text;
	private Type type;

	public SyntaxPart(String text, Type type) {
		this.text = text;
		this.type = type;
	}

    public Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SyntaxPart that = (SyntaxPart) o;

        if (!text.equals(that.text)) return false;
        if (type != that.type) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = text.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "SyntaxPart{" +
                "text='" + text + '\'' +
                ", type=" + type +
                '}';
    }

    public static enum Type {
		Keyword, Method, Comment, Bracket, Unclassified, Text, Quoted, UnclosedQuote
	}
}

