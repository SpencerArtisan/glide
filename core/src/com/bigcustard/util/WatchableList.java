package com.bigcustard.util;

import com.badlogic.gdx.utils.Disposable;
import com.bigcustard.scene2dplus.resource.Resource;
import com.google.common.collect.Lists;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WatchableList<E> implements Iterable<E>, Disposable {
    private final List<E> list;

    private WatchableValue<E> add = new WatchableValue<>(null);
    private WatchableValue<E> remove = new WatchableValue<>(null);

    public WatchableList(List<E> list) {
        this.list = Lists.newArrayList(list);
    }

    public boolean add(E e) {
        list.add(0, e);
        add.broadcast(e);
        return true;
    }

    public boolean remove(E e) {
        boolean result = list.remove(e);
        remove.broadcast(e);
        return result;
    }

    public void watchAdd(Consumer<E> watcher) {
        add.watch(watcher);
        list.forEach(watcher::accept);
    }

    public void watchRemove(Consumer<E> watcher) {
        remove.watch(watcher);
    }

    public void watchChange(Consumer<E> watcher) {
        remove.watch(watcher);
        add.watch(watcher);
        list.forEach(watcher::accept);
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

    public Stream<E> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    @Override
    public void dispose() {
        add.dispose();
        remove.dispose();
    }
}
