package com.bigcustard.planet.code;

import com.badlogic.gdx.graphics.Color;
import com.bigcustard.scene2dplus.textarea.ColorCoder;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

import static com.bigcustard.planet.code.SyntaxPart.Type.*;

public class CodeColorCoder implements ColorCoder {

    private static final Map<SyntaxPart.Type, String> DEFAULT_COLORS = ImmutableMap.<SyntaxPart.Type, String>builder().
            put(Unclassified, "#839496").
            put(Bracket, "#eee8d5").
            put(Brace, "WHITE").
            put(Keyword, "#b58900").
            put(Method, "#268bd2").
            put(Quoted, "#2aa198").
            put(UnclosedQuote, "RED").
            put(Comment, "#586e75").build();
    private static final String DEFAULT_ERROR = "dc322f88";

    private final Syntax syntax;
    private final Map<SyntaxPart.Type, String> colors;
    private final Color errorColor;

    public CodeColorCoder(Syntax syntax) {
        this(syntax, DEFAULT_COLORS, DEFAULT_ERROR);
    }

    @VisibleForTesting
    CodeColorCoder(Syntax syntax, Map<SyntaxPart.Type, String> colors, String errorColor) {
        this.syntax = syntax;
        this.colors = colors;
        this.errorColor = Color.valueOf(errorColor);
    }

    @Override
    public String encode(String program) {
        return FluentIterable
                .from(syntax.parse(program))
                .transform(this::colorCode)
                .join(Joiner.on(""));
    }

    @Override
    public Map<Integer, Color> colorLines(String program) {
        return Maps.asMap(syntax.errorLines(program), input -> errorColor);
    }

    private String colorCode(SyntaxPart syntaxPart) {
        return colors.containsKey(syntaxPart.type()) ?
                String.format("[%s]%s[]", colors.get(syntaxPart.type()), syntaxPart.text()) :
                syntaxPart.text();
    }
}