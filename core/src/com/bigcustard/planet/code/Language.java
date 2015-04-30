package com.bigcustard.planet.code;

import com.bigcustard.planet.language.GroovyKeywords;
import com.bigcustard.planet.language.Keywords;
import com.bigcustard.planet.language.RubyKeywords;
import com.bigcustard.planet.language.Syntax;
import com.bigcustard.scene2dplus.textarea.ColorCoder;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

public class Language {
    public static Language JRuby = new Language(new RubyKeywords(), "jruby");
    public static Language Groovy = new Language(new GroovyKeywords(), "groovy");

    private final Syntax syntax;
    private String scriptEngine;

    public Language(Keywords keywords, String scriptEngine) {
        this.scriptEngine = scriptEngine;
        this.syntax = new Syntax(keywords, scriptEngine);
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

    @Override
    public String toString() {
        return scriptEngine;
    }

    public static Language from(String scriptEngine) {
        if (scriptEngine.equals(JRuby.scriptEngine())) {
            return JRuby;
        } else if (scriptEngine.equals(Groovy.scriptEngine())) {
            return Groovy;
        }
        throw new IllegalArgumentException("Unknown language " + scriptEngine);
    }

    public String[] keywords() {
        return new String[0];
    }
}
