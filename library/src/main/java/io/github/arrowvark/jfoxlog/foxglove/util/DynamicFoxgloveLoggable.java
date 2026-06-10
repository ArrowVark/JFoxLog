package io.github.arrowvark.jfoxlog.foxglove.util;

import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.logging.Logger;

public class DynamicFoxgloveLoggable<T extends FoxgloveLoggable> implements FoxgloveLoggable {
    private final Supplier<T> root;
    private final String schema;
    private final String schemaName;
    private final List<Runnable> bindings;

    public DynamicFoxgloveLoggable(T root, List<Runnable> bindings) {
        this.root = () -> root;
        this.bindings = bindings;
        schema = root.getSchema();
        schemaName = root.getSchemaName();
    }

    public DynamicFoxgloveLoggable(Supplier<T> root) {
        this.root = root;
        schema = root.get().getSchema();
        schemaName = root.get().getSchemaName();
        bindings = null;
    }

    public DynamicFoxgloveLoggable(T root) {
        this.root = () -> root;
        schema = root.getSchema();
        schemaName = root.getSchemaName();
        bindings = null;
    }

    @Override
    public String getSchema() {
        return schema;
    }

    @Override
    public String getSchemaName() {
        return schemaName;
    }

    @Override
    public String toJson() {
        if (bindings != null) {
            bindings.forEach(Runnable::run);
        }
        return root.get().toJson();
    }

    @Override
    public DynamicFoxgloveLoggable<T> makeDynamic() {
        Logger.getGlobal().warning("Attempted to make a DynamicFoxgloveLoggable dynamic through the call \"makeDynamic\"");
        return this;
    }
}
