package com.bigcustard.planet.language;

public class JavascriptKeywords implements Keywords {
    @Override
    public String[] get() {
        return new String[] {"boolean", "break", "case", "catch", "char", "class",
                "const", "continue", "default", "do", "double", "else", "eval", "false", "finally", "float",
                "for", "function", "goto", "if", "in", "instanceof", "int", "long", "new", "null",
                "return", "switch", "this", "throw", "throws", "true", "try", "typeof", "var", "void", "while"};
    }

    @Override
    public String comment() {
        return "//";
    }
}
