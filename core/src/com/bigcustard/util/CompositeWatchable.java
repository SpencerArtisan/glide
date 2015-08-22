package com.bigcustard.util;

public class CompositeWatchable extends Notifier<Void> {
    private final Watchable<?>[] watchables;

    public CompositeWatchable(Watchable<?>... watchables) {
        this.watchables = watchables;
        for (Watchable<?> watchable : watchables) {
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
