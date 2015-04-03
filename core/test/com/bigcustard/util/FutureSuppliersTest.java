package com.bigcustard.util;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.SettableFuture;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

public class FutureSuppliersTest {
    @Test
    public void onGetSuccessful() {
        SettableFuture<String> settableFuture = SettableFuture.create();
        FutureSuppliers.onGet(() -> settableFuture, (result) -> {
            assertThat(result).isEqualTo("result");
        });

        settableFuture.set("result");
    }

    @Test
    public void onGetFails() {
        SettableFuture<String> settableFuture = SettableFuture.create();
        FutureSuppliers.onGet(() -> settableFuture, (result) -> {
            fail("Shouldn't get here");
        });

        settableFuture.setException(new Exception("bad stuff"));
    }

    @Test
    public void onGetCallbackFails() {
        SettableFuture<String> settableFuture = SettableFuture.create();
        FutureSuppliers.onGet(() -> settableFuture, (result) -> {
            throw new RuntimeException("bad stuff");
        });

        settableFuture.set("result");
    }
}
