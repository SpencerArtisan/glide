package com.bigcustard.util;

import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.button.ErrorHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Watchable<T> implements Disposable {
    private List<Consumer<T>> watchers = new ArrayList<>();

    public Watchable<T> watch(Consumer<T> watcher) {
        watchers.add(watcher);
        return this;
    }

    public void broadcast(T object) {
        ErrorHandler.tryAndRecover(() -> watchers.forEach((l) -> l.accept(object)));
    }

    @Override
    public void dispose() {
        watchers.clear();
    }
}
