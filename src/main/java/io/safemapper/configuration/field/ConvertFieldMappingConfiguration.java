package io.safemapper.configuration.field;

import io.safemapper.mapper.FieldMapper;
import io.safemapper.model.Getter;
import io.safemapper.model.Setter;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class ConvertFieldMappingConfiguration<TSource, TTarget, TSourceParameter, TTargetParameter>
        implements FieldMappingConfiguration<TSource, TTarget> {

    private final Setter<TTarget, TTargetParameter> setter;
    private final Getter<TSource, TSourceParameter> getter;
    private final Function<TSourceParameter,TTargetParameter> converter;

    public ConvertFieldMappingConfiguration(Setter<TTarget,TTargetParameter> setter,
                                            Getter<TSource,TSourceParameter> getter,
                                            Function<TSourceParameter,TTargetParameter> converter) {
        this.setter = setter;
        this.getter = getter;
        this.converter = converter;
    }

    @Override
    public FieldMapper<TSource, TTarget> build() {
        BiConsumer<TSource, TTarget> fieldMapperLambda = (source, target) -> {
            setter.set(target, converter.apply(getter.get(source)));
        };

        return new FieldMapper<>(fieldMapperLambda);
    }

    @Override
    public Setter<TTarget, ?> getSetter() {
        return this.setter;
    }
}
