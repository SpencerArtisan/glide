package com.bigcustard.util;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilTest {
    @Test
    public void tryGetWithoutException() {
        assertThat(Util.tryGet(() -> 42, 99)).isEqualTo(42);
    }

    @Test
    public void tryGetWithException() {
        assertThat(Util.tryGet(() -> {
            throw new RuntimeException();
        }, 99)).isEqualTo(99);
    }
}
