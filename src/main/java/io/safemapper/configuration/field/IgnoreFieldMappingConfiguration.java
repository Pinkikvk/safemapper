package io.safemapper.configuration.field;

import io.safemapper.mapper.FieldMapper;

import java.util.function.BiConsumer;

class IgnoreFieldMappingConfiguration<TSource, TTarget> implements FieldMappingConfiguration<TSource, TTarget> {

    private final BiConsumer<TTarget,?> setter;

    public IgnoreFieldMappingConfiguration(BiConsumer<TTarget,?> setter) {
        this.setter = setter;
    }

    @Override
    public FieldMapper<TSource, TTarget> build() {
        BiConsumer<TSource, TTarget> fieldMapperLambda = (source, target) -> {};
        return new FieldMapper<>(fieldMapperLambda);
    }

    @Override
    public BiConsumer<TTarget, ?> getSetter() {
        return this.setter;
    }
}
