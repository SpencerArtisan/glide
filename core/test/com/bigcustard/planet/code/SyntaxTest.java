package com.bigcustard.planet.code;

import com.bigcustard.planet.code.SyntaxPart;
import com.bigcustard.planet.language.GroovyKeywords;
import com.bigcustard.planet.language.Syntax;
import org.junit.Before;
import org.junit.Test;

import static com.bigcustard.planet.code.SyntaxPart.Type.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SyntaxTest {
	private Syntax syntax;
	
	@Before
	public void before() {
	    syntax = new Syntax(new GroovyKeywords());
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
	public void dot() throws Exception {
        assertThat(syntax.parse("blah.wibble")).containsExactly(
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart(".", Dot),
				new SyntaxPart("wibble", Unclassified));
	}

	@Test
	public void comma() throws Exception {
        assertThat(syntax.parse("blah, wibble")).containsExactly(
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart(",", Operator),
				new SyntaxPart(" wibble", Unclassified));
	}

	@Test
	public void equals() throws Exception {
        assertThat(syntax.parse("a == b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart("==", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void greaterThan() throws Exception {
        assertThat(syntax.parse("a > b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart(">", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void lessThan() throws Exception {
        assertThat(syntax.parse("a < b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart("<", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void greaterThanOrEqualTo() throws Exception {
        assertThat(syntax.parse("a >= b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart(">=", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void lessThanOrEqualTo() throws Exception {
        assertThat(syntax.parse("a <= b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart("<=", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void assign() throws Exception {
        assertThat(syntax.parse("a = b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart("=", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void add() throws Exception {
        assertThat(syntax.parse("a + b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart("+", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void subtract() throws Exception {
        assertThat(syntax.parse("a - b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart("-", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void divide() throws Exception {
        assertThat(syntax.parse("a / b")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" / ", Operator),
				new SyntaxPart("b", Unclassified));
	}

	@Test
	public void multiply() throws Exception {
        assertThat(syntax.parse("a * b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart("*", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void increment() throws Exception {
        assertThat(syntax.parse("a += b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart("+=", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void incrementByOne() throws Exception {
        assertThat(syntax.parse("a++")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart("++", Operator));
	}

	@Test
	public void decrementByOne() throws Exception {
        assertThat(syntax.parse("a--")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart("--", Operator));
	}

	@Test
	public void decrement() throws Exception {
        assertThat(syntax.parse("a -= b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart("-=", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void and() throws Exception {
        assertThat(syntax.parse("a && b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart("&&", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void or() throws Exception {
        assertThat(syntax.parse("a || b")).containsExactly(
				new SyntaxPart("a ", Unclassified),
				new SyntaxPart("||", Operator),
				new SyntaxPart(" b", Unclassified));
	}

	@Test
	public void bracket() throws Exception {
        assertThat(syntax.parse("(blah)")).containsExactly(
				new SyntaxPart("(", Bracket),
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart(")", Bracket));
	}

	@Test
	public void squareBracket() throws Exception {
        assertThat(syntax.parse("[blah]")).containsExactly(
				new SyntaxPart("[", SquareBracket),
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart("]", SquareBracket));
	}

	@Test
	public void doubleSquareBracket() throws Exception {
        assertThat(syntax.parse("[[blah]]")).containsExactly(
				new SyntaxPart("[", SquareBracket),
				new SyntaxPart("[", SquareBracket),
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart("]", SquareBracket),
				new SyntaxPart("]", SquareBracket));
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
	public void stringWithBrackets() throws Exception {
        assertThat(syntax.parse("prefix \"(quoted)\" suffix")).containsExactly(
                new SyntaxPart("prefix ", Unclassified),
                new SyntaxPart("\"(quoted)\"", Quoted),
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
