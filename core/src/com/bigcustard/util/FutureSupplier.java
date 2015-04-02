package com.bigcustard.util;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface FutureSupplier<T> extends Supplier<ListenableFuture<T>> {
}
