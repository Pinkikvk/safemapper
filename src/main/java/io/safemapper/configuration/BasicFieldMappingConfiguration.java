package io.safemapper.configuration;

import io.safemapper.mapper.FieldMapper;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class BasicFieldMappingConfiguration<TSource, TTarget, U> implements FieldMappingConfiguration<TSource, TTarget> {

    private final BiConsumer<TTarget,U> setter;
    private final Function<TSource,U> getter;

    public BasicFieldMappingConfiguration(BiConsumer<TTarget,U> setter, Function<TSource,U> getter) {
        this.setter = setter;
        this.getter = getter;
    }

    @Override
    public FieldMapper<TSource, TTarget> build() {
        BiConsumer<TSource, TTarget> fieldMapperLambda = (source, target) -> {
            setter.accept(target, getter.apply(source));
        };

        return new FieldMapper<>(fieldMapperLambda);
    }
}
