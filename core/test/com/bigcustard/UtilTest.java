package com.bigcustard;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilTest {
    @Test
    public void tryToWithoutException() {
        assertThat(Util.tryTo(() -> 42, 99)).isEqualTo(42);
    }

    @Test
    public void tryToWithException() {
        assertThat(Util.tryTo(() -> { throw new RuntimeException(); }, 99)).isEqualTo(99);
    }
}
