package com.bigcustard.scene2dplus.image;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Notifier<T> {
    private List<Consumer<T>> listeners = new ArrayList<>();

    public void add(Consumer<T> listener) {
        listeners.add(listener);
    }

    public void notify(T object) {
        listeners.forEach((l) -> l.accept(object));
    }
}
