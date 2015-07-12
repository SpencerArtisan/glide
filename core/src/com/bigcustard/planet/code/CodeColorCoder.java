package com.bigcustard.planet.code;

import com.badlogic.gdx.graphics.Color;
import com.bigcustard.planet.language.Syntax;
import com.bigcustard.scene2dplus.textarea.ColorCoder;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

import static com.bigcustard.planet.code.SyntaxPart.Type.*;

public class CodeColorCoder implements ColorCoder {

    private static final Map<SyntaxPart.Type, String> DEFAULT_COLORS = ImmutableMap.<SyntaxPart.Type, String>builder().
            put(Keyword, "#b58900").
            put(Operator, "#ffffff").
            put(Quoted, "#2aa198").
            put(UnclosedQuote, "#ff4444").
            put(Unclassified, "#839496").
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

    private String encodeSpecialCharacters(String program) {
        return program.replace("[", "[[");
    }

    private String colorCode(SyntaxPart syntaxPart) {
        String color = colors.get(syntaxPart.type());
        String text = encodeSpecialCharacters(syntaxPart.text());
        return colors.containsKey(syntaxPart.type()) ? String.format("[%s]%s[]", color, text) : text;
    }
}
