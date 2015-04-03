package com.bigcustard.util;

import java.util.function.Supplier;

public class Util {
    private Util() {}

    public static <T> T tryTo(Supplier<T> supplier, T fallback) {
        try {
            return supplier.get();
        } catch (Exception e) {
            return fallback;
        }
    }
}
