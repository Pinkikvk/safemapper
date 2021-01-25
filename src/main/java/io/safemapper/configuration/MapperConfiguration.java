package io.safemapper.configuration;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class MapperConfiguration<T, U> {
    private final Class<T> sourceClass;
    private final Class<U> destinationClass;

    private MapperConfiguration(Class<T> sourceClass, Class<U> destinationClass) {
        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;
    }

    public static <T,U> MapperConfiguration<T,U> create(Class<T> sourceClass, Class<U> destinationClass) {
        return new MapperConfiguration<>(sourceClass, destinationClass);
    }

    public <W> MapperConfiguration<T, U> ignore(BiConsumer<U,W> setter) {
        var a = setter.getClass();
        return this;
    }

    public <W> MapperConfiguration<T, U> addMapping(BiConsumer<U,W> setter, Function<T,W> getter) {
        return this;
    }

    public <W,X> MapperConfiguration<T, U> addMapping(BiConsumer<U,W> setter, Function<T,X> getter, Function<X,W> converter) {
        return this;
    }
}