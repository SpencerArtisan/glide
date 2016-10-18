package com.bigcustard.glide.code.language;

import com.bigcustard.glide.language.RubyKeywords;
import com.bigcustard.scene2dplus.textarea.TextAreaModel;
import org.apache.commons.lang3.tuple.Pair;
import org.jruby.RubyInstanceConfig;
import org.jruby.common.NullWarnings;
import org.jruby.lexer.yacc.LexerSource;
import org.jruby.parser.ParserConfiguration;
import org.jruby.parser.Ruby20Parser;
import org.jruby.parser.RubyParser;

import java.io.StringBufferInputStream;

public class Ruby extends Language {
    public static final String TEMPLATE = "##  My Game written by me!  2016";

    public Ruby() {
        super(new RubyKeywords(), "rb", "ruby-button", TEMPLATE);
    }

    @Override
    public Pair<Integer, String> errorChecker(String code) {
        try {
            RubyParser parser = new Ruby20Parser();
            parser.setWarnings(new NullWarnings(null));
            org.jruby.Ruby runtime = org.jruby.Ruby.getGlobalRuntime();
            RubyInstanceConfig rconfig = new RubyInstanceConfig();
            ParserConfiguration config = new ParserConfiguration(runtime, 0, false, false, true, rconfig);
            LexerSource lexer = LexerSource.getSource("code", new StringBufferInputStream(code), null, config);
            parser.parse(config, lexer);
        } catch (org.jruby.lexer.yacc.SyntaxException e) {
            return Pair.of(e.getPosition().getLine(), e.getMessage());
        } catch (Exception e) {
            System.out.println("Failed to parse code: " + e);
        }
        return null;
    }

    @Override
    public String vetoPreInsert(String characters, TextAreaModel textAreaModel) {
        if (currentLineEndsInDo(textAreaModel) && characters.matches("\n\\s*")) {
            characters = characters + "    $END$" + characters + "end";
        }
        return characters;
    }

    private boolean currentLineEndsInDo(TextAreaModel textAreaModel) {
        return textAreaModel.getCurrentLine().endsWith("do");
    }
}
