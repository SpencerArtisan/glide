package com.bigcustard.util;

import com.google.common.base.Objects;

import java.util.function.Consumer;

public class Watchable<T> extends Notifier<T> {
    private T value;

    public Watchable(T value) {
        this.value = value;
    }

    @Override
    public void watch(Consumer<T> listener) {
        super.watch(listener);
        notify(value);
    }

    public void set(T value) {
        this.value = value;
        notify(value);
    }

    public T get() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Watchable<?> watchable = (Watchable<?>) o;
        return Objects.equal(value, watchable.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Watchable{" +
                "value=" + value +
                '}';
    }
}
