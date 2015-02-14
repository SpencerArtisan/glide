package com.mygdx.game.groovy;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.mygdx.game.code.SyntaxPart;
import com.mygdx.game.textarea.ColorCoder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

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

    private GroovySyntax syntax;
    private Map<SyntaxPart.Type, String> colors;

    public GroovyColorCoder() {
        this(new GroovySyntax(), DEFAULT_COLORS);
    }

    public GroovyColorCoder(GroovySyntax syntax, Map<SyntaxPart.Type, String> colors) {
        this.syntax = syntax;
        this.colors = colors;
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
}
