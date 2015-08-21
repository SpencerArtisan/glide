package com.bigcustard.util;

import java.util.function.Consumer;

public class Watchable<T> extends Notifier<T> {
    private T value;

    public Watchable(T value) {
        this.value = value;
    }

    @Override
    public void add(Consumer<T> listener) {
        super.add(listener);
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
