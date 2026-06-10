package io.github.arrowvark.jfoxlog.foxglove.util;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public abstract class FoxgloveLoggableBuilder<T extends FoxgloveLoggable, Self extends FoxgloveLoggableBuilder<T, Self>> {
    protected final T instance;
    private final List<Runnable> bindings = new ArrayList<>();

    protected FoxgloveLoggableBuilder(T instance) {
        this.instance = instance;
    }

    private Self self() {
        return (Self) this;
    }

    protected <V> Self bind(Consumer<V> setter, V value) {
        setter.accept(value);
        return self();
    }

    protected <V> Self bind(Consumer<V> setter, Supplier<V> supplier) {
        setter.accept(supplier.get());
        bindings.add(() -> setter.accept(supplier.get()));
        return self();
    }

    protected abstract T constructLoggable();

    protected abstract DynamicFoxgloveLoggable<T> constructDynamicLoggable();

    protected List<Runnable> getBindings() {
        return bindings;
    }

    protected List<Runnable> collectBindings(List<Runnable> collector) {
        collector.addAll(bindings);
        return collector;
    }

    public void applyBindings() {
        bindings.forEach(Runnable::run);
    }
}
