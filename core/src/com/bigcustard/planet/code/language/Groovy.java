package com.bigcustard.planet.code.language;

import com.bigcustard.planet.language.GroovyKeywords;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Groovy extends Language {
    public static final String TEMPLATE =
                      "///////////////////////////////////// \n"
                    + "//       Welcome to GLIDE!         // \n"
                    + "//  Start writing your game below  // \n"
                    + "// Look at Samples for inspiration // \n"
                    + "///////////////////////////////////// \n\n";

    public Groovy() {
        super(new GroovyKeywords(), "groovy", "groovy-button", TEMPLATE);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Integer> errorLineChecker(String code) {
        Set<Integer> errorLines = new HashSet<>();
        try {
            new GroovyClassLoader().parseClass(code);
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
        } catch (Throwable e) {
            System.out.println("Failed to parse code: " + e);
        }
        return errorLines;
    }

    @Override
    public String vetoPreInsert(String characters, TextAreaModel textAreaModel) {
        if (currentLineEndsInOpeningBrace(textAreaModel) && characters.matches("\n\\s*")) {
            characters = characters + "    $END$" + characters + "}";
        }
        return characters;
    }

    private boolean currentLineEndsInOpeningBrace(TextAreaModel textAreaModel) {
        return textAreaModel.getCurrentLine().endsWith("{");
    }
}
