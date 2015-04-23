package com.bigcustard.planet.code;

import com.bigcustard.planet.plugin.groovy.GroovySyntax;
import com.bigcustard.planet.plugin.jruby.JRubySyntax;
import com.bigcustard.scene2dplus.textarea.ColorCoder;

public class Language {
    public static Language JRuby = new Language(new JRubySyntax(), "jruby");
    public static Language Groovy = new Language(new GroovySyntax(), "groovy");

    private Syntax syntax;
    private String scriptEngine;

    public Language(Syntax syntax, String scriptEngine) {
        this.syntax = syntax;
        this.scriptEngine = scriptEngine;
    }

    public boolean isValid(String code) {
        return syntax.isValid(code);
    }

    public ColorCoder codeColorCoder() {
        return new CodeColorCoder(syntax);
    }

    public String scriptEngine() {
        return scriptEngine;
    }
}
