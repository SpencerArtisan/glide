package com.bigcustard.planet.plugin.groovy;

import com.bigcustard.planet.code.SyntaxPart;
import org.junit.Before;
import org.junit.Test;

import static com.bigcustard.planet.code.SyntaxPart.Type.*;
import static org.assertj.core.api.Assertions.assertThat;

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

    @Test
    public void noErrorsInGoodGroovy() {
        assertThat(syntax.errorLines("public void hello() {\n}")).isEmpty();
    }

    @Test
    public void goodGroovyCodeIsValid() {
        assertThat(syntax.isValid("public void hello() {\n}")).isTrue();
    }

    @Test
    public void errorsInBadGroovy() {
        assertThat(syntax.errorLines("public void hello() {\n\"unended string\n}")).containsExactly(1);
    }

	@Test
	public void badGroovyCodeIsIsInvalid() {
		assertThat(syntax.isValid("public void hello() {\n\"unended string\n}")).isFalse();
	}
}