package com.bigcustard.planet.code;

import com.bigcustard.planet.language.GroovyKeywords;
import com.bigcustard.planet.language.RubyKeywords;
import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

import java.io.StringBufferInputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Ruby extends Language {
    public static final String TEMPLATE =
                      "############################################## \n"
                    + "##         Welcome to Planet Burpl!         ## \n"
                    + "##      Start writing your game below       ## \n"
                    + "## Look in the Game Library for inspiration ## \n"
                    + "############################################## \n\n";

    public Ruby() {
        super(new RubyKeywords(), "ruby", "ruby-button", TEMPLATE);
    }

    @Override
    public Set<Integer> errorLineChecker(String code) {
        Set<Integer> errorLines = new HashSet<>();
//        try {
//            RubyParser parser = new Ruby20Parser();
//            parser.setWarnings(new NullWarnings(null));
//            Ruby runtime = Ruby.getGlobalRuntime();
//            RubyInstanceConfig rconfig = new RubyInstanceConfig();
//            ParserConfiguration config = new ParserConfiguration(runtime, 0, false, false, true, rconfig);
//            LexerSource lexer = LexerSource.getSource("code", new StringBufferInputStream(program), null, config);
//            parser.parse(config, lexer);
//        } catch (org.jruby.lexer.yacc.SyntaxException e) {
//            errorLines.add(e.getPosition().getLine());
//        } catch (Exception e) {
//            System.out.println("Failed to parse code: " + e);
//        }
        return errorLines;
    }
}
