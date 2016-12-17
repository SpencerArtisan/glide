package com.bigcustard.glide.language;

public class PythonKeywords implements Keywords {
    @Override
    public String[] get() {
        return new String[] {"def", "and", "or", "break", "case", "catch", "char", "class",
                "const", "continue", "default", "do", "double", "else", "eval", "False", "finally", "float",
                "for", "function", "goto", "if", "elif", "else", "in", "instanceof", "int", "long", "new", "null",
                "return", "switch", "this", "throw", "throws", "True", "try", "typeof", "var", "void", "while",
                "max", "min", "str", "len", "range", "count", "is", "not", "None", "global"};

    }

    @Override
    public String comment() {
        return "#";
    }
}
