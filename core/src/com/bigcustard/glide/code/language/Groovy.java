package com.bigcustard.glide.code.language;

import com.bigcustard.glide.language.GroovyKeywords;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import groovy.lang.GroovyClassLoader;
import org.apache.commons.lang3.tuple.Pair;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Groovy extends Language {
    public static final String TEMPLATE = "//  My Game written by me!  2016";

    public Groovy() {
        super(new GroovyKeywords(), "groovy", "groovy-button", TEMPLATE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Pair<Integer, String> errorChecker(String code) {
        try {
            new GroovyClassLoader().parseClass(code);
        } catch (MultipleCompilationErrorsException e) {
            List<Message> errors = e.getErrorCollector().getErrors();
            if (errors.size() > 0) {
                if (errors.get(0) instanceof SyntaxErrorMessage) {
                    SyntaxException cause = ((SyntaxErrorMessage) errors.get(0)).getCause();
                    int errorLine = cause.getLine();
                    return Pair.of(errorLine - 1, cause.getOriginalMessage());
                } else {
                    throw e;
                }
            }
        } catch (Throwable e) {
            System.out.println("Failed to parse code: " + e);
        }
        return null;
    }

    @Override
    public String vetoPreInsert(String characters, TextAreaModel textAreaModel) {
        if (currentLineEndsInOpeningBrace(textAreaModel) && characters.matches("\n\\s*")) {
            characters = characters + "    $END$";
        }
        return characters;
    }

    private boolean currentLineEndsInOpeningBrace(TextAreaModel textAreaModel) {
        return textAreaModel.getCurrentLine().endsWith("{");
    }
}
