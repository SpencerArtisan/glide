package com.mygdx.game.textarea;

import static org.assertj.core.api.Assertions.*;
import com.google.common.collect.ImmutableMap;
import com.mygdx.game.code.SyntaxPart;
import com.mygdx.game.groovy.GroovyColorCoder;
import com.mygdx.game.groovy.GroovySyntax;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;

import static com.mygdx.game.code.SyntaxPart.Type.*;
import static org.mockito.Mockito.when;

public class GroovyColorCoderTest {
	private GroovyColorCoder coder;
	@Mock private GroovySyntax syntax;
	
	@Before
	public void before() {
        MockitoAnnotations.initMocks(this);
		coder = new GroovyColorCoder(syntax, ImmutableMap.of(Keyword, "BLUE", Bracket, "WHITE", Comment, "GRAY", Quoted, "RED"));
	}

	@Test
	public void unclassified() throws Exception {
		when(syntax.parse("wibble")).thenReturn(Arrays.asList(
				new SyntaxPart("wibble", Unclassified)));
		assertThat(coder.encode("wibble")).isEqualTo("wibble");
	}
	
	@Test
	public void keyword() throws Exception {
		when(syntax.parse("public wibble")).thenReturn(Arrays.asList(
				new SyntaxPart("public", Keyword), 
				new SyntaxPart(" wibble", Unclassified)));
		assertThat(coder.encode("public wibble")).isEqualTo("[BLUE]public[] wibble");
	}
	
	@Test
	public void bracket() throws Exception {
		when(syntax.parse("(blah)")).thenReturn(Arrays.asList(
				new SyntaxPart("(", Bracket), 
				new SyntaxPart("blah", Unclassified),
				new SyntaxPart(")", Bracket))); 
		assertThat(coder.encode("(blah)")).isEqualTo("[WHITE]([]blah[WHITE])[]");
	}
	
	@Test
	public void comment() throws Exception {
		when(syntax.parse("// Comment\nNon comment")).thenReturn(Arrays.asList(
				new SyntaxPart("// Comment", Comment), 
				new SyntaxPart("\nNon comment", Unclassified))); 
		assertThat(coder.encode("// Comment\nNon comment")).isEqualTo("[GRAY]// Comment[]\nNon comment");
	}
	
	@Test
	public void string() throws Exception {
		when(syntax.parse("prefix \"quoted\" suffix")).thenReturn(Arrays.asList(
				new SyntaxPart("prefix ", Unclassified), 
				new SyntaxPart("\"quoted\"", Quoted), 
				new SyntaxPart(" suffix", Unclassified))); 
		assertThat(coder.encode("prefix \"quoted\" suffix")).isEqualTo("prefix [RED]\"quoted\"[] suffix");
	}	
}
