package com.bigcustard.planet.code;

import com.bigcustard.planet.language.GroovyKeywords;
import com.bigcustard.planet.language.JavascriptKeywords;
import com.bigcustard.planet.language.Keywords;
import com.bigcustard.planet.language.Syntax;
import com.bigcustard.scene2dplus.textarea.ColorCoder;
import com.google.common.base.Function;
import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class Language {
    public static Language Groovy = new Groovy();
    public static Language Javascript = new Javascript();

    private final Syntax syntax;
    private String scriptEngine;
    private String buttonStyle;
    private String template;

    Language(Keywords keywords, String scriptEngine, String buttonStyle, String template) {
        this.scriptEngine = scriptEngine;
        this.buttonStyle = buttonStyle;
        this.template = template;
        this.syntax = new Syntax(keywords, this::errorLineChecker);
    }

    public abstract Set<Integer> errorLineChecker(String code);

    public boolean isValid(String code) {
        return syntax.isValid(code);
    }

    public ColorCoder codeColorCoder() {
        return new CodeColorCoder(syntax);
    }

    public String scriptEngine() {
        return scriptEngine;
    }

    public String buttonStyle() {
        return buttonStyle;
    }

    public Syntax syntax() {
        return syntax;
    }

    public String template() {
        return template;
    }

    @Override
    public String toString() {
        return scriptEngine;
    }

    public static Language from(String scriptEngine) {
        if (scriptEngine.equals(Javascript.scriptEngine())) {
            return Javascript;
        } else if (scriptEngine.equals(Groovy.scriptEngine())) {
            return Groovy;
        }
        throw new IllegalArgumentException("Unknown language " + scriptEngine);
    }
}
