package io.github.hudsoncrisp.jfoxlog.foxglove.util;

import java.util.Objects;
import java.util.function.Supplier;

public class DynamicValue<T> {
    private final Supplier<T> supplier;

    private DynamicValue(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        return supplier.get();
    }

    public static <T> DynamicValue<T> fixed(T value) {
        Objects.requireNonNull(value);
        return new DynamicValue<>(() -> value);
    }

    public static <T> DynamicValue<T> variable(Supplier<T> supplier) {
        return new DynamicValue<>(supplier);
    }
}
