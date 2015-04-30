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
import org.codehaus.groovy.syntax.SyntaxException;

import java.util.*;

import static com.bigcustard.planet.code.SyntaxPart.Type.*;

public class Syntax {
    private static String[] TOKENS = new String[] {" ", "\t", "\n", "\r", "\f", "(", ")", "{", "}", "\"", ".", "[", "]"};
    private static String[] OPERATORS = new String[] {"==", "<", ">", "<=", ">=", "!=", "=", "++", "--", "+=",
                                                      "-=", "+", "-", " / ", "*", "&&", "||", ","};
    private Keywords languageKeywords;

    public Syntax(Keywords languageKeywords) {
        this.languageKeywords = languageKeywords;
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
            new GroovyClassLoader().parseClass(program);
        } catch (MultipleCompilationErrorsException e) {
            List<Message> errors = e.getErrorCollector().getErrors();
            for (Message error : errors) {
                if (error instanceof SyntaxErrorMessage) {
                    SyntaxException cause = ((SyntaxErrorMessage) error).getCause();
                    int errorLine = cause.getLine();
                //    System.out.println("Error: " + cause.getMessage());
                    errorLines.add(errorLine - 1);
                } else {
                    throw e;
                }
            }
        } catch (Exception e) {
            // Something unexpected when awry
            System.out.println("Failed to parse code: " + e);
        }
        return errorLines;
    }

    @SuppressWarnings("unchecked")
    private List<SyntaxPart> categoriseWordsIntoTypes(String program) {
        String[] deliminators = ObjectArrays.concat(TOKENS, OPERATORS, String.class);
        List<String> wordsAndSpaces = new Tokenizer(program, deliminators).run();
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

                if (lastElement.type() != Brace &&
                        lastElement.type() != Bracket &&
                        lastElement.type() != SquareBracket &&
                        lastElement.type() != Dot &&
                        lastElement.type() == newElement.type()) {
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
        } else if (word.equals("(") || word.equals(")")) {
            return Bracket;
        } else if (word.equals("[") || word.equals("]")) {
            return SquareBracket;
        } else if (word.equals("{") || word.equals("}")) {
            return Brace;
        } else if (word.startsWith("//")) {
            return Comment;
        } else if (word.equals("\"")) {
            return UnclosedQuote;
        } else if (word.equals(".")) {
            return Dot;
        } else if (Arrays.asList(OPERATORS).contains(word)) {
            return Operator;
        }
        return Unclassified;
    }

    private String[] keywords() {
        return ObjectArrays.concat(new FrameworkKeywords().get(), languageKeywords.get(), String.class);
    }
}
