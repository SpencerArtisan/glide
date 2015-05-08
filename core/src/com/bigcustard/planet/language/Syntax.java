package com.bigcustard.planet.language;

import com.bigcustard.planet.code.SyntaxPart;
import com.bigcustard.util.Tokenizer;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static com.bigcustard.planet.code.SyntaxPart.Type.*;

public class Syntax {
    private static String[] TOKENS = new String[] {
            " ", "\t", "\n", "\r", "\f", "(", ")", "{", "}", "\"", ".", "[", "]", "==", "<", ">", "<=", ">=",
            "!=", "=", "++", "--", "+=", "-=", "+", "-", " / ", "*", "&&", "||", ",", "$", "%", ";"};
    private Keywords languageKeywords;
    private Function<String, Set<Integer>> errorLineChecker;

    public Syntax(Keywords languageKeywords, Function<String, Set<Integer>> errorLineChecker) {
        this.languageKeywords = languageKeywords;
        this.errorLineChecker = errorLineChecker;
    }

    public List<SyntaxPart> parse(String program) {
        List<SyntaxPart> classifiedWordsAndSpaces = categoriseWordsIntoTypes(program);
        return collapseAdjacentPartsWithSameType(classifiedWordsAndSpaces);
    }

    public boolean isValid(String program) {
        return errorLines(program).isEmpty();
    }

    public Set<Integer> errorLines(String program) {
        return errorLineChecker.apply(program);
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
        } else if (word.startsWith(languageKeywords.comment())) {
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
