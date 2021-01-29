package io.safemapper.configuration;

import io.safemapper.mapper.FieldMapper;

public interface FieldMappingConfiguration<TSource, TTarget> {
    FieldMapper<TSource, TTarget> build();
}
