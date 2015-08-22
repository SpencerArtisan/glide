package com.bigcustard.util;

public class CompositeWatchable extends Watchable<Void> {
    private final WatchableValue<?>[] watchables;

    public CompositeWatchable(WatchableValue<?>... watchables) {
        this.watchables = watchables;
        for (WatchableValue<?> watchable : watchables) {
            watchable.watch((ignored) -> broadcast());
        }
    }

    public void watch(Runnable listener) {
        super.watch((ignored) -> listener.run());
        broadcast();
    }

    public void broadcast() {
        broadcast(null);
    }
}
