package com.bigcustard.util;

import org.junit.Ignore;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenizerTest {

    @Test
    @Ignore
    public void itShould_ReturnNoValuesForEmptyStringAndNoDelimiters() {
        List<String> result = new Tokenizer("", new String[0]).run();
        assertThat(result).isEmpty();
    }

    @Test
    public void itShould_ReturnNoValuesForEmptyStringAndDelimiters() {
        List<String> result = new Tokenizer("", new String[]{":", ","}).run();
        assertThat(result).isEmpty();
    }

    @Test
    public void itShould_ReturnSingleValueForStringWithoutDelimiters() {
        List<String> result = new Tokenizer("hello", new String[]{":", ","}).run();
        assertThat(result).containsExactly("hello");
    }

    @Test
    public void itShould_ReturnDelimitedValuesAndDelimiter() {
        List<String> result = new Tokenizer("hello,world", new String[]{":", ","}).run();
        assertThat(result).containsExactly("hello", ",", "world");
    }
}
