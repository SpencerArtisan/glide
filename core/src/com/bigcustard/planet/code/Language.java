package com.bigcustard.planet.code;

import com.bigcustard.planet.language.GroovyKeywords;
import com.bigcustard.planet.language.Keywords;
import com.bigcustard.planet.language.RubyKeywords;
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

public class Language {
    public static Language JRuby = new Language(new RubyKeywords(), "jruby", (s) -> new HashSet<>());
    public static Language Groovy = new Language(new GroovyKeywords(), "groovy", (program) -> {
        Set<Integer> errorLines = new HashSet<>();
        try {
            new GroovyClassLoader().parseClass(program);
        } catch (MultipleCompilationErrorsException e) {
            List<Message> errors = e.getErrorCollector().getErrors();
            for (Message error : errors) {
                if (error instanceof SyntaxErrorMessage) {
                    SyntaxException cause = ((SyntaxErrorMessage) error).getCause();
                    int errorLine = cause.getLine();
                    errorLines.add(errorLine - 1);
                } else {
                    throw e;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to parse code: " + e);
        }
        return errorLines;
    });

    private final Syntax syntax;
    private String scriptEngine;

    public Language(Keywords keywords, String scriptEngine, Function<String, Set<Integer>> errorLineChecker) {
        this.scriptEngine = scriptEngine;
        this.syntax = new Syntax(keywords, errorLineChecker);
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

    public Syntax syntax() {
        return syntax;
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
}
