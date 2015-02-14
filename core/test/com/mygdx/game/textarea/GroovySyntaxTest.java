package com.mygdx.game.textarea;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.mygdx.game.code.SyntaxPart;
import com.mygdx.game.groovy.GroovyColorCoder;
import com.mygdx.game.groovy.GroovySyntax;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Iterator;
import java.util.StringTokenizer;

import static com.mygdx.game.code.SyntaxPart.Type.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class GroovySyntaxTest {
	private GroovySyntax syntax;
	
	@Before
	public void before() {
	    syntax = new GroovySyntax();
    }

	@Test
	public void unclassified() throws Exception {
		assertThat(syntax.parse("wibble")).containsExactly(new SyntaxPart("wibble", Unclassified));
    }
	
	@Test
	public void keyword() throws Exception {
        assertThat(syntax.parse("public wibble")).containsExactly(
				new SyntaxPart("public", Keyword),
				new SyntaxPart(" wibble", Unclassified));
	}

	@Test
	public void bracket() throws Exception {
        assertThat(syntax.parse("(blah)")).containsExactly(
				new SyntaxPart("(", Bracket),
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart(")", Bracket));
	}

	@Test
	public void brace() throws Exception {
        assertThat(syntax.parse("{blah}")).containsExactly(
				new SyntaxPart("{", Brace),
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart("}", Brace));
	}

	@Test
	public void comment() throws Exception {
        assertThat(syntax.parse("// Comment\nNon comment")).containsExactly(
				new SyntaxPart("// Comment", Comment),
				new SyntaxPart("\nNon comment", Unclassified));
	}

	@Test
	public void longComment() throws Exception {
        assertThat(syntax.parse("////")).containsExactly(
				new SyntaxPart("////", Comment));
	}

	@Test
	public void string() throws Exception {
        assertThat(syntax.parse("prefix \"quoted\" suffix")).containsExactly(
				new SyntaxPart("prefix ", Unclassified),
				new SyntaxPart("\"quoted\"", Quoted),
				new SyntaxPart(" suffix", Unclassified));
	}
}
