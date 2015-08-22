package com.bigcustard.glide.code.language;

import com.bigcustard.glide.language.RubyKeywords;
import org.apache.commons.lang3.tuple.Pair;

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
    public Pair<Integer, String> errorChecker(String code) {
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
//            error.watch(e.getPosition().getLine());
//        } catch (Exception e) {
//            System.out.println("Failed to parse code: " + e);
//        }
        return null;
    }
}
