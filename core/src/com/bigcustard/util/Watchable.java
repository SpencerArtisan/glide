package com.bigcustard.util;

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
}
