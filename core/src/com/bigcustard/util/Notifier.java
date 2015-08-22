package com.bigcustard.util;

import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Notifier<T> implements Disposable {
    private List<Consumer<T>> listeners = new ArrayList<>();

    private static int count;

    public Notifier() {
        System.out.println("notifiers = " + ++count);
    }

    public void watch(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void notify(T object) {
        listeners.forEach((l) -> l.accept(object));
    }

    @Override
    public void dispose() {
        listeners.clear();
        System.out.println("notifiers = " + --count);
    }
}
