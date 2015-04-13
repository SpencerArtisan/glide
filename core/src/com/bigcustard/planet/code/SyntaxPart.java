package com.bigcustard.planet.code;


public class SyntaxPart {
	private String text;
	private Type type;

	public SyntaxPart(String text, Type type) {
		this.text = text;
		this.type = type;
	}

    public Type type() {
        return type;
    }

    public String text() {
        return text;
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

    public enum Type {
		Keyword, Method, Comment, Bracket, Brace, Unclassified, Quoted, UnclosedQuote, Dot, Operator
	}
}

