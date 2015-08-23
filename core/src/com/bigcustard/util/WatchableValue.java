package com.bigcustard.util;

import com.google.common.base.Objects;

import java.util.function.Consumer;

public class WatchableValue<T> extends Watchable<T> {
    private T value;

    public WatchableValue(T value) {
        this.value = value;
    }

    @Override
    public void watch(Consumer<T> watcher) {
        super.watch(watcher);
    }

    public void set(T value) {
        this.value = value;
        broadcast(value);
    }

    public T get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WatchableValue<?> watchable = (WatchableValue<?>) o;
        return Objects.equal(value, watchable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "WatchableValue{value=" + value + '}';
    }
}
