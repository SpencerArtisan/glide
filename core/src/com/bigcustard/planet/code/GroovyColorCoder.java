package com.bigcustard.planet.code;

import com.badlogic.gdx.graphics.Color;
import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.bigcustard.scene2dplus.textarea.ColorCoder;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GroovyColorCoder implements ColorCoder {

    private static final Map<SyntaxPart.Type, String> DEFAULT_COLORS = ImmutableMap.<SyntaxPart.Type, String>builder().
            put(SyntaxPart.Type.Unclassified, "#839496").
            put(SyntaxPart.Type.Bracket, "#eee8d5").
            put(SyntaxPart.Type.Brace, "WHITE").
            put(SyntaxPart.Type.Keyword, "#b58900").
            put(SyntaxPart.Type.Method, "#268bd2").
            put(SyntaxPart.Type.Quoted, "#2aa198").
            put(SyntaxPart.Type.UnclosedQuote, "RED").
            put(SyntaxPart.Type.Comment, "#586e75").build();
    private static final String DEFAULT_ERROR = "dc322f88";

    private final GroovySyntax syntax;
    private final Map<SyntaxPart.Type, String> colors;
    private final String errorColor;

    public GroovyColorCoder() {
        this(new GroovySyntax(), DEFAULT_COLORS, DEFAULT_ERROR);
    }

    public GroovyColorCoder(GroovySyntax syntax, Map<SyntaxPart.Type, String> colors, String errorColor) {
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
                return colors.containsKey(syntaxPart.getType()) ?
                        String.format("[%s]%s[]", colors.get(syntaxPart.getType()), syntaxPart.getText()) :
                        syntaxPart.getText();
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
