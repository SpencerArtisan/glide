package com.bigcustard.planet.code;

import com.bigcustard.planet.plugin.groovy.GroovySyntax;
import com.bigcustard.planet.plugin.jruby.JRubySyntax;
import com.bigcustard.scene2dplus.textarea.ColorCoder;

public enum Language {
    JRuby(new JRubySyntax(), "jruby"),
    Groovy(new GroovySyntax(), "groovy");

    private Syntax syntax;
    private String scriptEngine;

    private Language(Syntax syntax, String scriptEngine) {
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
