package com.bigcustard.planet.language;

import com.bigcustard.planet.code.Language;
import com.bigcustard.planet.code.SyntaxPart;
import com.bigcustard.util.Tokenizer;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.jsr223.GroovyScriptEngineImpl;
import org.codehaus.groovy.syntax.SyntaxException;
import org.jruby.embed.jsr223.JRubyCompiledScript;
import org.jruby.embed.jsr223.JRubyScriptEngineManager;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.net.URLClassLoader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bigcustard.planet.code.SyntaxPart.Type.*;

public class Syntax {
    private static String[] TOKENS = new String[] {
            " ", "\t", "\n", "\r", "\f", "(", ")", "{", "}", "\"", ".", "[", "]", "==", "<", ">", "<=", ">=",
            "!=", "=", "++", "--", "+=", "-=", "+", "-", " / ", "*", "&&", "||", ","};
    private Keywords languageKeywords;
    private ScriptEngine scriptEngine;

    public Syntax(Keywords languageKeywords, String scriptEngine) {
        this(languageKeywords, new ScriptEngineManager().getEngineByName(scriptEngine));
    }

    Syntax(Keywords languageKeywords, ScriptEngine scriptEngine) {
        this.languageKeywords = languageKeywords;
        this.scriptEngine = scriptEngine;
    }

    public List<SyntaxPart> parse(String program) {
        List<SyntaxPart> classifiedWordsAndSpaces = categoriseWordsIntoTypes(program);
        return collapseAdjacentPartsWithSameType(classifiedWordsAndSpaces);
    }

    public boolean isValid(String program) {
        return errorLines(program).isEmpty();
    }

    public Set<Integer> errorLines(String program) {
        Set<Integer> errorLines = new HashSet<>();
        try {
            // todo - can this block the thread?
            scriptEngine.eval(program);
        } catch (ScriptException e) {
            String message = e.getMessage();
            Matcher matcher = Pattern.compile("(?s).*:[ ]*(\\d+):.*").matcher(message);
            boolean matches = matcher.matches();
            if (matches) {
                String group = matcher.group(1);
                int lineNumber = Integer.parseInt(group);
                errorLines.add(lineNumber - 1);
            }
        }
        return errorLines;
    }

    @SuppressWarnings("unchecked")
    private List<SyntaxPart> categoriseWordsIntoTypes(String program) {
        List<String> wordsAndSpaces = new Tokenizer(program, TOKENS).run();
        return Lists.transform(wordsAndSpaces, new Function<String, SyntaxPart>() {
            public SyntaxPart apply(String word) {
                return new SyntaxPart(word, getType(word));
            }
        });
    }

    private List<SyntaxPart> collapseAdjacentPartsWithSameType(List<SyntaxPart> classifiedWordsAndSpaces) {
        List<SyntaxPart> collapsed = new ArrayList<>();
        for (SyntaxPart newElement : classifiedWordsAndSpaces) {
            if (!collapsed.isEmpty()) {
                SyntaxPart lastElement = collapsed.get(collapsed.size() - 1);
                if (lastElement.type() == Comment && !newElement.text().contains("\n")) {
                    newElement.setType(Comment);
                }
                if (lastElement.type() == UnclosedQuote) {
                    if (newElement.type() == UnclosedQuote) {
                        lastElement.setType(Quoted);
                        newElement.setType(Quoted);
                    } else {
                        newElement.setType(UnclosedQuote);
                    }
                }

                if (lastElement.type() == newElement.type()) {
                    newElement = new SyntaxPart(lastElement.text() + newElement.text(), newElement.type());
                    collapsed.remove(collapsed.size() - 1);
                }

            }
            collapsed.add(newElement);
        }
        return collapsed;
    }

    private SyntaxPart.Type getType(String word) {
        if (Sets.newHashSet(keywords()).contains(word)) {
            return Keyword;
        } else if (word.startsWith("//")) {
            return Comment;
        } else if (word.equals("\"")) {
            return UnclosedQuote;
        } else if (Arrays.asList(TOKENS).contains(word)) {
            return Operator;
        }
        return Unclassified;
    }

    private String[] keywords() {
        return ObjectArrays.concat(new FrameworkKeywords().get(), languageKeywords.get(), String.class);
    }
}
