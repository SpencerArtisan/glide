package com.bigcustard.planet.language;

import com.bigcustard.planet.code.SyntaxPart;
import com.bigcustard.util.Tokenizer;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.ObjectArrays;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.tuple.Pair;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;

import static com.bigcustard.planet.code.SyntaxPart.Type.*;

public class Syntax {
    private static String[] TOKENS = new String[] {
            " ", "\t", "\n", "\r", "\f", "(", ")", "{", "}", "\"", ".", "[", "]", "==", "<", ">", "<=", ">=",
            "!", "!=", "=", "++", "*=", "/=", "--", "+=", "-=", "+", "-", " / ", "*", "&&", "||", ",", "$", "%", ";"};
    private Keywords languageKeywords;
    private Function<String, Pair<Integer, String>> errorChecker;

    private AtomicReference<Pair<Integer, String>> lastKnownResult = new AtomicReference<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(2);
    private Future<?> futureSyntaxCheck;

    public Syntax(Keywords languageKeywords, Function<String, Pair<Integer, String>> errorChecker) {
        this.languageKeywords = languageKeywords;
        this.errorChecker = errorChecker;
    }

    public List<SyntaxPart> parse(String program) {
        List<SyntaxPart> classifiedWordsAndSpaces = categoriseWordsIntoTypes(program);
        return collapseAdjacentPartsWithSameType(classifiedWordsAndSpaces);
    }

    public boolean isValid(String program) {
        return error(program) == null;
    }

    public Pair<Integer, String> error(String program) {
        if (futureSyntaxCheck == null || futureSyntaxCheck.isDone()) {
            futureSyntaxCheck = executorService.submit(() -> {
                Pair<Integer, String> error = errorChecker.apply(program);
                lastKnownResult.set(error);
            });
        }
        return lastKnownResult.get();
    }

    @SuppressWarnings("unchecked")
    private List<SyntaxPart> categoriseWordsIntoTypes(String program) {
        List<String> wordsAndSpaces = new Tokenizer(program, TOKENS).run();
        return Lists.transform(wordsAndSpaces, word -> new SyntaxPart(word, getType(word)));
    }

    private List<SyntaxPart> collapseAdjacentPartsWithSameType(List<SyntaxPart> classifiedWordsAndSpaces) {
        List<SyntaxPart> collapsed = new ArrayList<>();
        for (SyntaxPart newElement : classifiedWordsAndSpaces) {
            if (!collapsed.isEmpty()) {
                SyntaxPart lastElement = collapsed.get(collapsed.size() - 1);
                if (lastElement.type() == Comment && !newElement.text().contains("\n")) {
                    newElement.type(Comment);
                }
                if (lastElement.type() == UnclosedQuote) {
                    if (newElement.type() == UnclosedQuote) {
                        lastElement.type(Quoted);
                        newElement.type(Quoted);
                    } else {
                        newElement.type(UnclosedQuote);
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
