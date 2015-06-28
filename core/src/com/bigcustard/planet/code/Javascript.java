package com.bigcustard.planet.code;

import com.bigcustard.planet.language.GroovyKeywords;
import com.bigcustard.planet.language.JavascriptKeywords;
import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Javascript extends Language {
    public static final String TEMPLATE =
              "////////////////////////////////////////////// \n"
            + "//         Welcome to Planet Burpl!         // \n"
            + "//      Start writing your game below       // \n"
            + "// Look in the Game Library for inspiration // \n"
            + "////////////////////////////////////////////// \n\n";

    public Javascript() {
        super(new JavascriptKeywords(), "js", "javascript-button", TEMPLATE);
    }

    @Override
    public Set<Integer> errorLineChecker(String code) {
        return new HashSet<>();
    }
}
