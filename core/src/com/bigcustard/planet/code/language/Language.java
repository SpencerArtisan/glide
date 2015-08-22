package com.bigcustard.planet.code.language;

import com.bigcustard.planet.code.CodeColorCoder;
import com.bigcustard.planet.language.Keywords;
import com.bigcustard.planet.language.Syntax;
import com.bigcustard.scene2dplus.textarea.ColorCoder;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import org.apache.commons.lang3.tuple.Pair;

public abstract class Language {
    public static Language Ruby = new Ruby();
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
        this.syntax = new Syntax(keywords, this::errorChecker);
    }

    public abstract Pair<Integer, String> errorChecker(String code);

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

    public String vetoPreInsert(String characters, TextAreaModel textAreaModel) {
        return characters;
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
        } else if (scriptEngine.equals(Ruby.scriptEngine())) {
            return Ruby;
        }
        throw new IllegalArgumentException("Unknown language " + scriptEngine);
    }
}
