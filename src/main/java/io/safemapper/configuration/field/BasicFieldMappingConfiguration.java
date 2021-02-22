package io.safemapper.configuration.field;

import io.safemapper.mapper.FieldMapper;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class BasicFieldMappingConfiguration<TSource, TTarget, TParameter> implements FieldMappingConfiguration<TSource, TTarget> {

    private final BiConsumer<TTarget, TParameter> setter;
    private final Function<TSource, TParameter> getter;

    public BasicFieldMappingConfiguration(BiConsumer<TTarget, TParameter> setter, Function<TSource, TParameter> getter) {
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

    @Override
    public BiConsumer<TTarget, ?> getSetter() {
        return this.setter;
    }
}
