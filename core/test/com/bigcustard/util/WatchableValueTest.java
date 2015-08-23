package com.bigcustard.util;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

public class WatchableValueTest {
    private WatchableValue<String> subject = new WatchableValue<>("hello");

    @Test
    public void itShouldNot_NotifyNewListenersImmediately() {
        subject.watch((v) -> fail());
    }

    @Test
    public void itShould_NotifyListenersWhenValueChanges() {
        AtomicReference<String> value = new AtomicReference<>();
        subject.watch(value::set);
        subject.set("world");
        assertThat(value.get()).isEqualTo("world");
    }

    @Test
    public void itShould_NotifyListenersOfNullValueChange() {
        AtomicReference<String> value = new AtomicReference<>();
        subject.watch(value::set);
        subject.set(null);
        assertThat(value.get()).isNull();
    }

}
