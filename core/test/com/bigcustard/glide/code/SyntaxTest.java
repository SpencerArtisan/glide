package com.bigcustard.glide.code;

import com.bigcustard.glide.code.language.Language;
import com.bigcustard.glide.language.Syntax;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Before;
import org.junit.Test;

import static com.bigcustard.glide.code.SyntaxPart.Type.*;
import static org.assertj.core.api.Assertions.assertThat;

public class SyntaxTest {
	private Syntax syntax;
	
	@Before
	public void before() {
	    useGroovySyntax();
    }

	@Test
	public void unclassified() throws Exception {
		assertThat(syntax.parse("wibble")).containsExactly(new SyntaxPart("wibble", Unclassified));
    }
	
	@Test
	public void keyword() throws Exception {
        assertThat(syntax.parse("public wibble")).containsExactly(
				new SyntaxPart("public", Keyword),
				new SyntaxPart(" ", Operator),
				new SyntaxPart("wibble", Unclassified));
	}

	@Test
	public void dot() throws Exception {
        assertThat(syntax.parse("blah.wibble")).containsExactly(
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart(".", Operator),
				new SyntaxPart("wibble", Unclassified));
	}
	
	@Test
	public void colon() throws Exception {
        assertThat(syntax.parse("blah:wibble")).containsExactly(
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart(":", Operator),
				new SyntaxPart("wibble", Unclassified));
	}

	@Test
	public void comma() throws Exception {
        assertThat(syntax.parse("blah, wibble")).containsExactly(
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart(", ", Operator),
				new SyntaxPart("wibble", Unclassified));
	}

	@Test
	public void equals() throws Exception {
        assertThat(syntax.parse("a == b")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" == ", Operator),
				new SyntaxPart("b", Unclassified));
	}

	@Test
	public void greaterThan() throws Exception {
        assertThat(syntax.parse("a > b")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" > ", Operator),
				new SyntaxPart("b", Unclassified));
	}

	@Test
	public void lessThan() throws Exception {
        assertThat(syntax.parse("a < b")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" < ", Operator),
				new SyntaxPart("b", Unclassified));
	}

	@Test
	public void greaterThanOrEqualTo() throws Exception {
        assertThat(syntax.parse("a >= b")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" >= ", Operator),
				new SyntaxPart("b", Unclassified));
	}

	@Test
	public void lessThanOrEqualTo() throws Exception {
        assertThat(syntax.parse("a <= b")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" <= ", Operator),
				new SyntaxPart("b", Unclassified));
	}

	@Test
	public void assign() throws Exception {
        assertThat(syntax.parse("a = b")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" = ", Operator),
				new SyntaxPart("b", Unclassified));
	}

	@Test
	public void add() throws Exception {
        assertThat(syntax.parse("a + b")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" + ", Operator),
				new SyntaxPart("b", Unclassified));
	}

	@Test
	public void subtract() throws Exception {
        assertThat(syntax.parse("a - b")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" - ", Operator),
				new SyntaxPart("b", Unclassified));
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
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" * ", Operator),
				new SyntaxPart("b", Unclassified));
	}

	@Test
	public void increment() throws Exception {
        assertThat(syntax.parse("a += b")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" += ", Operator),
				new SyntaxPart("b", Unclassified));
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
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" -= ", Operator),
				new SyntaxPart("b", Unclassified));
	}

	@Test
	public void and() throws Exception {
        assertThat(syntax.parse("a && b")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" && ", Operator),
				new SyntaxPart("b", Unclassified));
	}

	@Test
	public void or() throws Exception {
        assertThat(syntax.parse("a || b")).containsExactly(
				new SyntaxPart("a", Unclassified),
				new SyntaxPart(" || ", Operator),
				new SyntaxPart("b", Unclassified));
	}

	@Test
	public void bracket() throws Exception {
        assertThat(syntax.parse("(blah)")).containsExactly(
				new SyntaxPart("(", Operator),
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart(")", Operator));
	}

	@Test
	public void squareBracket() throws Exception {
        assertThat(syntax.parse("[blah]")).containsExactly(
				new SyntaxPart("[", Operator),
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart("]", Operator));
	}

	@Test
	public void doubleSquareBracket() throws Exception {
        assertThat(syntax.parse("[[blah]]")).containsExactly(
				new SyntaxPart("[[", Operator),
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart("]]", Operator));
	}

	@Test
	public void brace() throws Exception {
        assertThat(syntax.parse("{blah}")).containsExactly(
				new SyntaxPart("{", Operator),
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart("}", Operator));
	}

	@Test
	public void comment() throws Exception {
        assertThat(syntax.parse("// Comment\nNon comment")).containsExactly(
				new SyntaxPart("// Comment", Comment),
				new SyntaxPart("\n", Operator),
				new SyntaxPart("Non", Unclassified),
				new SyntaxPart(" ", Operator),
				new SyntaxPart("comment", Unclassified));
	}

	@Test
	public void longComment() throws Exception {
        assertThat(syntax.parse("////")).containsExactly(
				new SyntaxPart("////", Comment));
	}

	@Test
	public void string() throws Exception {
        assertThat(syntax.parse("prefix \"quoted\" suffix")).containsExactly(
				new SyntaxPart("prefix", Unclassified),
				new SyntaxPart(" ", Operator),
				new SyntaxPart("\"quoted\"", Quoted),
				new SyntaxPart(" ", Operator),
				new SyntaxPart("suffix", Unclassified));
	}

	@Test
	public void stringWithBrackets() throws Exception {
        assertThat(syntax.parse("prefix \"(quoted)\" suffix")).containsExactly(
				new SyntaxPart("prefix", Unclassified),
				new SyntaxPart(" ", Operator),
				new SyntaxPart("\"(quoted)\"", Quoted),
				new SyntaxPart(" ", Operator),
				new SyntaxPart("suffix", Unclassified));
	}

    @Test
    public void noErrorsInGoodGroovy() throws InterruptedException {
		String program = "public void hello() {\n}";
		syntax.isValid(program);
		Thread.sleep(5000);
		assertThat(syntax.error(program)).isNull();
    }

    @Test
    public void goodGroovyCodeIsValid() throws InterruptedException {
		String program = "public void hello() {\n}";
		syntax.isValid(program);
		Thread.sleep(5000);
		assertThat(syntax.isValid(program)).isTrue();
    }

    @Test
    public void errorsInBadGroovy() throws InterruptedException {
		String program = "public void hello() {\n\"unended string\n}";
		syntax.isValid(program);
		Thread.sleep(5000);
		assertThat(syntax.error(program)).isEqualTo(Pair.of(1, "expecting anything but ''\\n''; got it anyway"));
    }

	@Test
	public void badGroovyCodeIsIsInvalid() throws InterruptedException {
		String program = "public void hello() {\n\"unended string\n}";
		syntax.isValid(program);
		Thread.sleep(5000);
		assertThat(syntax.isValid(program)).isFalse();
	}

//    @Test
//    public void noErrorsInGoodJavascript() {
//		useJavascriptSyntax();
//        assertThat(syntax.error("function hello() {\n}")).isEmpty();
//    }
//
//    @Test
//    public void goodJavascriptCodeIsValid() {
//		useJavascriptSyntax();
//        assertThat(syntax.isValid("function hello() {\n}")).isTrue();
//    }
//
//    @Test
//    public void errorsInBadJavascript() {
//		useJavascriptSyntax();
//        assertThat(syntax.error("function hello() {\n\"unended string\n}")).containsExactly(1);
//    }
//
//	@Test
//	public void badJavascriptCodeIsIsInvalid() {
//		useJavascriptSyntax();
//		assertThat(syntax.isValid("function hello() {\n\"unended string\n}")).isFalse();
//	}

//	@Test
//	public void noErrorsInGoodRuby() {
//		useRubySyntax();
//		assertThat(syntax.error("puts 'hi'")).isEmpty();
//	}
//
//	@Test
//	public void goodRubyCodeIsValid() {
//		useRubySyntax();
//		assertThat(syntax.isValid("puts 'hi'")).isTrue();
//	}
//
//	@Test
//	public void errorsInBadRuby() {
//		useRubySyntax();
//		assertThat(syntax.error("puts 'unended string")).containsExactly(0);
//	}
//
//	@Test
//	public void badRubyCodeIsIsInvalid() {
//		useRubySyntax();
//		assertThat(syntax.isValid("puts 'unended string")).isFalse();
//	}
//
	private void useGroovySyntax() {
		syntax = Language.Groovy.syntax();
	}

	private void useJavascriptSyntax() {
		syntax = Language.Javascript.syntax();
	}

//	private void useRubySyntax() {
//		syntax = Language.JRuby.syntax();
//	}

}
