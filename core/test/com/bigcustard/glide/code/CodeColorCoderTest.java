package com.bigcustard.glide.code;

import com.badlogic.gdx.graphics.Color;
import com.bigcustard.glide.language.Syntax;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.data.MapEntry;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static com.bigcustard.glide.code.SyntaxPart.Type.Keyword;
import static com.bigcustard.glide.code.SyntaxPart.Type.Operator;
import static com.bigcustard.glide.code.SyntaxPart.Type.Unclassified;
import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CodeColorCoderTest {
	private CodeColorCoder coder;
	@Mock private Syntax syntax;

	@Before
	public void before() {
        MockitoAnnotations.initMocks(this);
		coder = new CodeColorCoder(() -> null, syntax, ImmutableMap.of(Keyword, "BLUE", Operator, "WHITE"), "ff0000", "ff0000");
	}

	@Test
	public void openSquareBracket() throws Exception {
		when(syntax.parse("[")).thenReturn(singletonList(
				new SyntaxPart("[", Operator)));
		assertThat(coder.encode("[")).isEqualTo("[WHITE][[[]");
	}

	@Test
	public void doubleOpenSquareBracket() throws Exception {
		when(syntax.parse("[[")).thenReturn(singletonList(
				new SyntaxPart("[[", Operator)));
		assertThat(coder.encode("[[")).isEqualTo("[WHITE][[[[[]");
	}

	@Test
	public void doubleCloseSquareBracket() throws Exception {
		when(syntax.parse("]]")).thenReturn(singletonList(
				new SyntaxPart("]]", Operator)));
		assertThat(coder.encode("]]")).isEqualTo("[WHITE]]][]");
	}

	@Test
	public void openThenCloseSquareBracket() throws Exception {
		when(syntax.parse("[]")).thenReturn(singletonList(
				new SyntaxPart("[]", Operator)));
		assertThat(coder.encode("[]")).isEqualTo("[WHITE][[]][]");
	}

	@Test
	public void closeSquareBracket() throws Exception {
		when(syntax.parse("]")).thenReturn(singletonList(
				new SyntaxPart("]", Operator)));
		assertThat(coder.encode("]")).isEqualTo("[WHITE]][]");
	}

	@Test
	public void unspecifiedColor() throws Exception {
		when(syntax.parse("word")).thenReturn(singletonList(
				new SyntaxPart("word", Unclassified)));
		assertThat(coder.encode("word")).isEqualTo("word");
	}

    @Test
    public void singleElement() throws Exception {
        when(syntax.parse("word")).thenReturn(singletonList(
                new SyntaxPart("word", Keyword)));
        assertThat(coder.encode("word")).isEqualTo("[BLUE]word[]");
    }

    @Test
	public void multipleElements() throws Exception {
		when(syntax.parse("word1 ==")).thenReturn(Arrays.asList(
				new SyntaxPart("word1", Keyword),
				new SyntaxPart(" ==", Operator)));
		assertThat(coder.encode("word1 ==")).isEqualTo("[BLUE]word1[][WHITE] ==[]");
	}

    @Test
    public void colorErrorLine() {
        when(syntax.error("hello\nthere")).thenReturn(Pair.of(1, ""));
        assertThat(coder.colorLines("hello\nthere"))
				.containsExactly(MapEntry.<String, Color>entry(1, Color.valueOf("ff0000")));
    }

    @Test
    public void doNotColorValidLine() {
        when(syntax.error("hello\nthere")).thenReturn(null);
        assertThat(coder.colorLines("hello\nthere")).isEmpty();
    }
}
