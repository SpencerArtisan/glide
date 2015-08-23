package com.bigcustard.util;

import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Watchable<T> implements Disposable {
    private List<Consumer<T>> watchers = new ArrayList<>();

    private static int count;

    public Watchable() {
        System.out.println("watchables = " + ++count + this);
    }

    public Watchable<T> watch(Consumer<T> watcher) {
        watchers.add(watcher);
        return this;
    }

    public void broadcast(T object) {
        watchers.forEach((l) -> l.accept(object));
    }

    @Override
    public void dispose() {
        watchers.clear();
        System.out.println("watchables = " + --count + this);
    }
}
