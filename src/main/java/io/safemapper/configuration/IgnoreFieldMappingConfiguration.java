package io.safemapper.configuration;

import io.safemapper.mapper.FieldMapper;

import java.util.function.BiConsumer;
import java.util.function.Function;

class IgnoreFieldMappingConfiguration<TSource, TTarget> implements FieldMappingConfiguration<TSource, TTarget> {

    @Override
    public FieldMapper<TSource, TTarget> build() {
        BiConsumer<TSource, TTarget> fieldMapperLambda = (source, target) -> {};
        return new FieldMapper<>(fieldMapperLambda);
    }
}
