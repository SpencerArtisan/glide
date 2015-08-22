package com.bigcustard.glide.language;

public class GroovyKeywords implements Keywords {
    @Override
    public String[] get() {
        return new String[] {"public", "boolean", "break", "case", "catch", "char", "class", "const", "continue", "do",
                "double", "else", "enum", "extends", "final", "finally", "float", "for", "goto", "if",
                "implements", "import", "int", "interface", "long", "new", "package", "private", "protected",
                "public", "return", "static", "super", "switch", "this", "throw", "throws", "try", "void", "while",
                "true", "false", "in", "add", "any", "it", "size" };
    }

    @Override
    public String comment() {
        return "//";
    }
}
