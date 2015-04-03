package com.bigcustard.util;

import java.util.function.Supplier;

public class Util {
    private Util() {}

    public static <T> T tryGet(Supplier<T> supplier, T fallback) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return fallback;
        }
    }
}
