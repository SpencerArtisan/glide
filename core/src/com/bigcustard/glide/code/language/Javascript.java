package com.bigcustard.glide.code.language;

import com.bigcustard.glide.language.JavascriptKeywords;
import org.apache.commons.lang3.tuple.Pair;

public class Javascript extends Language {
    public static final String TEMPLATE = "//  My Game written by me!  2016";

    public Javascript() {
        super(new JavascriptKeywords(), "js", "javascript-button", TEMPLATE);
    }

    @Override
    public Pair<Integer, String> errorChecker(String code) {
        return null;
    }
}
