package com.bigcustard.planet.code;

import com.badlogic.gdx.graphics.Color;
import com.bigcustard.scene2dplus.textarea.ColorCoder;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    private final String errorColor;

    public CodeColorCoder(Syntax syntax) {
        this(syntax, DEFAULT_COLORS, DEFAULT_ERROR);
    }

    CodeColorCoder(Syntax syntax, Map<SyntaxPart.Type, String> colors, String errorColor) {
        this.syntax = syntax;
        this.colors = colors;
        this.errorColor = errorColor;
    }

    @Override
    public String encode(String program) {
        List<SyntaxPart> parsed = syntax.parse(program);
        List<String> parts = Lists.transform(parsed, new Function<SyntaxPart, String>() {
            @Override
            public String apply(SyntaxPart syntaxPart) {
                return colors.containsKey(syntaxPart.type()) ?
                        String.format("[%s]%s[]", colors.get(syntaxPart.type()), syntaxPart.text()) :
                        syntaxPart.text();
            }
        });
        return StringUtils.join(parts.toArray());
    }

    @Override
    public Map<Integer, Color> colorLines(String program) {
        Set<Integer> errorLines = syntax.errorLines(program);
        Map<Integer, Color> coloredLines = new HashMap<Integer, Color>();
        for (Integer errorLine : errorLines) {
            coloredLines.put(errorLine, Color.valueOf(errorColor));
        }
        return coloredLines;
    }
}
