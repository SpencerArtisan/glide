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

public class Language {
//    public static Language JRuby = new Language(new RubyKeywords(), "jruby", "ruby-button",
//                      "############################################## \n"
//                    + "##         Welcome to Planet Burpl!         ## \n"
//                    + "##      Start writing your game below       ## \n"
//                    + "## Look in the Game Library for inspiration ## \n"
//                    + "############################################## \n\n",
//            (program) -> {
//                Set<Integer> errorLines = new HashSet<>();
//
//                try {
//                    RubyParser parser = new Ruby20Parser();
//                    parser.setWarnings(new NullWarnings(null));
//                    Ruby runtime = Ruby.getGlobalRuntime();
//                    RubyInstanceConfig rconfig = new RubyInstanceConfig();
//                    ParserConfiguration config = new ParserConfiguration(runtime, 0, false, false, true, rconfig);
//                    LexerSource lexer = LexerSource.getSource("code", new StringBufferInputStream(program), null, config);
//                    parser.parse(config, lexer);
//                } catch (org.jruby.lexer.yacc.SyntaxException e) {
//                    errorLines.add(e.getPosition().getLine());
//                } catch (Exception e) {
//                    System.out.println("Failed to parse code: " + e);
//                }
//
//
//                return errorLines;
//            });
    public static Language Groovy = new Language(new GroovyKeywords(), "groovy", "groovy-button",
                      "////////////////////////////////////////////// \n"
                    + "//         Welcome to Planet Burpl!         // \n"
                    + "//      Start writing your game below       // \n"
                    + "// Look in the Game Library for inspiration // \n"
                    + "////////////////////////////////////////////// \n\n",
                    (program) -> {
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
    public static Language Javascript = new Language(new JavascriptKeywords(), "js", "javascript-button",
                      "////////////////////////////////////////////// \n"
                    + "//         Welcome to Planet Burpl!         // \n"
                    + "//      Start writing your game below       // \n"
                    + "// Look in the Game Library for inspiration // \n"
                    + "////////////////////////////////////////////// \n\n",
                    (program) -> {
                            Set<Integer> errorLines = new HashSet<>();

                            return errorLines;
                        });

    private final Syntax syntax;
    private String scriptEngine;
    private String buttonStyle;
    private String template;

    public Language(Keywords keywords, String scriptEngine, String buttonStyle, String template, Function<String, Set<Integer>> errorLineChecker) {
        this.scriptEngine = scriptEngine;
        this.buttonStyle = buttonStyle;
        this.template = template;
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
