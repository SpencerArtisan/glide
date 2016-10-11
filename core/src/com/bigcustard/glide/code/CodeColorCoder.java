package com.bigcustard.glide.code;

import com.badlogic.gdx.graphics.Color;
import com.bigcustard.glide.language.Syntax;
import com.bigcustard.scene2dplus.textarea.ColorCoder;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Joiner;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;

import static com.bigcustard.glide.code.SyntaxPart.Type.*;

public class CodeColorCoder implements ColorCoder {

    private static final Map<SyntaxPart.Type, String> DEFAULT_COLORS = ImmutableMap.<SyntaxPart.Type, String>builder().
            put(Keyword, "#b58900").
            put(Operator, "#ffffff").
            put(Quoted, "#2aa198").
            put(UnclosedQuote, "#ff4444").
            put(Unclassified, "#839496").
            put(Comment, "#586e75").build();
    private static final String DEFAULT_ERROR = "dc322f88";
    private static final String DEFAULT_RUNTIME_ERROR = "ed211f88";

    private final Game game;
    private final Syntax syntax;
    private final Map<SyntaxPart.Type, String> colors;
    private final Color errorColor;
    private final Color runtimeErrorColor;

    public CodeColorCoder(Game game, Syntax syntax) {
        this(game, syntax, DEFAULT_COLORS, DEFAULT_ERROR, DEFAULT_RUNTIME_ERROR);
    }

    @VisibleForTesting
    CodeColorCoder(Game game, Syntax syntax, Map<SyntaxPart.Type, String> colors, String errorColor, String runtimeErrorColor) {
        this.game = game;
        this.syntax = syntax;
        this.colors = colors;
        this.errorColor = Color.valueOf(errorColor);
        this.runtimeErrorColor = Color.valueOf(runtimeErrorColor);
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
        HashMap<Integer, Color> lineColours = new HashMap<>();
        Pair<Integer, String> error = syntax.error(program);
        if (error != null) {
            lineColours.put(error.getLeft(), errorColor);
        }
        Pair<Integer, String> runtimeError = game.runtimeError();
        if (runtimeError != null) {
            lineColours.put(runtimeError.getLeft() - 1, runtimeErrorColor);
        }
        return lineColours;
    }

    private String encodeSpecialCharacters(String program) {
        return program.replace("[", "[[").replace("]", "]]");
    }

    private String colorCode(SyntaxPart syntaxPart) {
        String color = colors.get(syntaxPart.type());
        String text = encodeSpecialCharacters(syntaxPart.text());
        return colors.containsKey(syntaxPart.type()) ? String.format("[%s]%s[]", color, text) : text;
    }
}
