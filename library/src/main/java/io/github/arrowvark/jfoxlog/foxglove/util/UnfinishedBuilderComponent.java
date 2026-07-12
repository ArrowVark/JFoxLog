//package io.github.arrowvark.jfoxlog.foxglove.util;
//
//import io.github.arrowvark.jfoxlog.foxglove.FoxgloveLoggable;
//
//import java.util.List;
//import java.util.function.Supplier;
//
//public class UnfinishedBuilderComponent<T extends FoxgloveLoggable, Self extends FoxgloveLoggableBuilder<T, Self>> {
//    private FoxgloveLoggableBuilder<T, Self> builder;
//    private DynamicFoxgloveLoggable<T> finalizedLoggable;
//
//    public UnfinishedBuilderComponent(FoxgloveLoggableBuilder<T, Self> builder) {
//        this.builder = builder;
//    }
//
//    public UnfinishedBuilderComponent(DynamicFoxgloveLoggable<T> finalizedLoggable) {
//        this.finalizedLoggable = finalizedLoggable;
//    }
//
//    public UnfinishedBuilderComponent(T finalizedLoggable) {
//        this.finalizedLoggable = new DynamicFoxgloveLoggable<>(() -> finalizedLoggable);
//    }
//
//    public List<Runnable> getBindings() {
//        return  builder.getBindings();
//    }
//
//    public List<Runnable> collectBindings(List<Runnable> collector) {
//        collector.addAll(getBindings());
//        return collector;
//    }
//
//    public T finish() {
//        if (builder == null) { return null; }
//        T loggable = builder.constructLoggable();
//        finalizedLoggable = new DynamicFoxgloveLoggable<>(loggable);
//        return loggable;
//    }
//
//    public DynamicFoxgloveLoggable<T> finishDynamic() {
//        if (builder == null) { return null; }
//        DynamicFoxgloveLoggable<T> loggable = builder.constructDynamicLoggable();
//        finalizedLoggable = loggable;
//        return loggable;
//    }
//}
