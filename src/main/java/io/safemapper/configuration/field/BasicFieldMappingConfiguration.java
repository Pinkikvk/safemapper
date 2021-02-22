package io.safemapper.configuration.field;

import io.safemapper.mapper.FieldMapper;
import io.safemapper.model.Getter;
import io.safemapper.model.Setter;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class BasicFieldMappingConfiguration<TSource, TTarget, TParameter> implements FieldMappingConfiguration<TSource, TTarget> {

    private final Setter<TTarget, TParameter> setter;
    private final Getter<TSource, TParameter> getter;

    public BasicFieldMappingConfiguration(Setter<TTarget, TParameter> setter, Getter<TSource, TParameter> getter) {
        this.setter = setter;
        this.getter = getter;
    }

    @Override
    public FieldMapper<TSource, TTarget> build() {
        BiConsumer<TSource, TTarget> fieldMapperLambda = (source, target) -> {
            setter.set(target, getter.get(source));
        };

        return new FieldMapper<>(fieldMapperLambda);
    }

    @Override
    public Setter<TTarget, ?> getSetter() {
        return this.setter;
    }
}
