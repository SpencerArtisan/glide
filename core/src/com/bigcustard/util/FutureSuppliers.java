package com.bigcustard.util;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.function.Consumer;

public class FutureSuppliers {
    public static <T> void onGet(FutureSupplier<T> supplier, Consumer<T> callback) {
        ListenableFuture<T> future = supplier.get();
        Futures.addCallback(future, new FutureCallback<T>() {
            @Override
            public void onSuccess(T result) {
                callback.accept(result);
            }

            @Override
            public void onFailure(Throwable t) {
                System.err.println(t.getMessage());
            }
        });
    }
}
