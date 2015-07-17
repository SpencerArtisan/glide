package com.bigcustard.planet.code.language;

import com.bigcustard.planet.language.RubyKeywords;

import java.util.HashSet;
import java.util.Set;

public class Ruby extends Language {
    public static final String TEMPLATE =
                      "##################################### \n"
                    + "##       Welcome to GLIDE!         ## \n"
                    + "##  Start writing your game below  ## \n"
                    + "## Look at Samples for inspiration ## \n"
                    + "##################################### \n\n";

    public Ruby() {
        super(new RubyKeywords(), "rb", "ruby-button", TEMPLATE);
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
