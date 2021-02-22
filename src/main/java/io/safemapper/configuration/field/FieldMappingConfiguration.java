package io.safemapper.configuration.field;

import io.safemapper.mapper.FieldMapper;

import java.util.function.BiConsumer;

public interface FieldMappingConfiguration<TSource, TTarget> {
    FieldMapper<TSource, TTarget> build();

    BiConsumer<TTarget, ?> getSetter();
}
