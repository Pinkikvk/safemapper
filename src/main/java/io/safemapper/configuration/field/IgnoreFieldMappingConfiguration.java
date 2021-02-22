package io.safemapper.configuration.field;

import io.safemapper.mapper.FieldMapper;
import io.safemapper.model.Setter;

import java.util.function.BiConsumer;

public class IgnoreFieldMappingConfiguration<TSource, TTarget> implements FieldMappingConfiguration<TSource, TTarget> {

    private final Setter<TTarget,?> setter;

    public IgnoreFieldMappingConfiguration(Setter<TTarget,?> setter) {
        this.setter = setter;
    }

    @Override
    public FieldMapper<TSource, TTarget> build() {
        BiConsumer<TSource, TTarget> fieldMapperLambda = (source, target) -> {};
        return new FieldMapper<>(fieldMapperLambda);
    }

    @Override
    public Setter<TTarget, ?> getSetter() {
        return this.setter;
    }
}
