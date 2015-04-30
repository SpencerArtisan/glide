package com.bigcustard.planet.language;

public class RubyKeywords implements Keywords {
    @Override
    public String[] get() {
        return new String[] {"and", "begin", "break", "case", "class", "def", "do",
                "else", "elsif", "end", "false", "for", "if", "in", "nil", "not", "or",
                "new", "rescue", "return", "self", "super", "then", "true", "unless", "while"};
    }
}
