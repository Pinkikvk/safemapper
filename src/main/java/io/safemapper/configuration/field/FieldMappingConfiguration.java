package io.safemapper.configuration.field;

import io.safemapper.mapper.FieldMapper;
import io.safemapper.model.Setter;

public interface FieldMappingConfiguration<TSource, TTarget> {
    FieldMapper<TSource, TTarget> build();

    Setter<TTarget, ?> getSetter();
}
