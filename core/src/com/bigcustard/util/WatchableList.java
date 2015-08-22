package com.bigcustard.util;

import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;

public class WatchableList<E> extends Watchable<List<E>> implements Iterable<E> {
    private final List<E> list;

    public WatchableList(List<E> list) {
        this.list = Lists.newArrayList(list);
    }

    public boolean add(E e) {
        list.add(0, e);
        broadcast(list);
        return true;
    }

    public boolean remove(Object o) {
        boolean remove = list.remove(o);
        broadcast(list);
        return remove;
    }

    @Override
    public void watch(Consumer<List<E>> watcher) {
        super.watch(watcher);
        broadcast(list);
    }

    @Override
    public Iterator<E> iterator() {
        return list.iterator();
    }

    @Override
    public void forEach(Consumer<? super E> action) {
        list.forEach(action);
    }

    @Override
    public Spliterator<E> spliterator() {
        return list.spliterator();
    }
}
