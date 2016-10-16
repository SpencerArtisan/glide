package com.bigcustard.glide.language;

public class PythonKeywords implements Keywords {
    @Override
    public String[] get() {
        return new String[] {"def", "and", "or", "break", "case", "catch", "char", "class",
                "const", "continue", "default", "do", "double", "else", "eval", "false", "finally", "float",
                "for", "function", "goto", "if", "elif", "else", "in", "instanceof", "int", "long", "new", "null",
                "return", "switch", "this", "throw", "throws", "true", "try", "typeof", "var", "void", "while",
                "max", "min", "str"};

    }

    @Override
    public String comment() {
        return "#";
    }
}
