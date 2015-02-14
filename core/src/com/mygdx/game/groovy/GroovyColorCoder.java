package com.mygdx.game.groovy;

import com.google.common.base.Function;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mygdx.game.code.SyntaxPart;
import com.mygdx.game.textarea.ColorCoder;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

public class GroovyColorCoder implements ColorCoder {
    private GroovySyntax syntax;
    private Map<SyntaxPart.Type, String> colors;

    public GroovyColorCoder() {}

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
