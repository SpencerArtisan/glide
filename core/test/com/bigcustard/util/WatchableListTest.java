package com.bigcustard.util;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;

public class WatchableListTest {
    private WatchableList<String> subject;

    @Before
    public void before() {
        subject = new WatchableList<>(ImmutableList.of("one", "two"));
    }

    @Test
    public void itShould_NotifyNewListenersImmediately() {
        AtomicReference<List<String>> value = new AtomicReference<>();
        subject.watch(value::set);
        assertThat(value.get()).containsExactly("one", "two");
    }

    @Test
    public void itShould_NotifyListenersWhenValueAdded() {
        AtomicReference<List<String>> value = new AtomicReference<>();
        subject.watch(value::set);
        subject.add("three");
        assertThat(value.get()).containsExactly("three", "one", "two");
    }

    @Test
    public void itShould_NotifyListenersWhenValueRemoved() {
        AtomicReference<List<String>> value = new AtomicReference<>();
        subject.watch(value::set);
        subject.remove("one");
        assertThat(value.get()).containsExactly("two");
    }


}
