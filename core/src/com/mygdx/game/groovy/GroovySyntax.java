package com.mygdx.game.groovy;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.mygdx.game.code.SyntaxPart;
import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.messages.Message;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.syntax.SyntaxException;

import java.util.*;

import static com.mygdx.game.code.SyntaxPart.Type.*;

public class GroovySyntax {
    private static final Set KEYWORDS = ImmutableSet.of("public");

    public List<SyntaxPart> parse(String program) {
        List<SyntaxPart> classifiedWordsAndSpaces = categoriseWordsIntoTypes(program);
        return collapseAdjacentPartsWithSameType(classifiedWordsAndSpaces);
    }

    public Set<Integer> errorLines(String program) {
        Set<Integer> errorLines = new HashSet<Integer>();
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
        }
        return errorLines;
    }

    @SuppressWarnings("unchecked")
    private List<SyntaxPart> categoriseWordsIntoTypes(String program) {
        List wordsAndSpaces = Collections.list(new StringTokenizer(program, " \t\n\r\f(){}\"", true));
        return Lists.transform(wordsAndSpaces, new Function<String, SyntaxPart>() {
            public SyntaxPart apply(String word) {
                return new SyntaxPart(word, getType(word));
            }
        });
    }

    private List<SyntaxPart> collapseAdjacentPartsWithSameType(List<SyntaxPart> classifiedWordsAndSpaces) {
        List<SyntaxPart> collapsed = new ArrayList<SyntaxPart>();
        for (SyntaxPart newElement : classifiedWordsAndSpaces) {
            if (!collapsed.isEmpty()) {
                SyntaxPart lastElement = collapsed.get(collapsed.size() - 1);
                if (lastElement.getType() == Comment && !newElement.getText().contains("\n")) {
                    newElement.setType(Comment);
                }
                if (lastElement.getType() == UnclosedQuote) {
                    if (newElement.getType() == UnclosedQuote) {
                        lastElement.setType(Quoted);
                        newElement.setType(Quoted);
                    } else {
                        newElement.setType(UnclosedQuote);
                    }
                }

                if (lastElement.getType() == newElement.getType()) {
                    newElement = new SyntaxPart(lastElement.getText() + newElement.getText(), newElement.getType());
                    collapsed.remove(collapsed.size() - 1);
                }

            }
            collapsed.add(newElement);
        }
        return collapsed;
    }

    private SyntaxPart.Type getType(String word) {
        if (KEYWORDS.contains(word)) {
            return Keyword;
        } else if (word.equals("(") || word.equals(")")) {
            return Bracket;
        } else if (word.equals("{") || word.equals("}")) {
            return Brace;
        } else if (word.startsWith("//")) {
            return Comment;
        } else if (word.equals("\"")) {
            return UnclosedQuote;
        }
        return Unclassified;
    }
}
