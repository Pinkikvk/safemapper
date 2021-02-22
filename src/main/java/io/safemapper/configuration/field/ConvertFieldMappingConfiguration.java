package io.safemapper.configuration.field;

import io.safemapper.mapper.FieldMapper;

import java.util.function.BiConsumer;
import java.util.function.Function;

class ConvertFieldMappingConfiguration<TSource, TTarget, TSourceParameter, TTargetParameter>
        implements FieldMappingConfiguration<TSource, TTarget> {

    private final BiConsumer<TTarget, TTargetParameter> setter;
    private final Function<TSource, TSourceParameter> getter;
    private final Function<TSourceParameter,TTargetParameter> converter;

    public ConvertFieldMappingConfiguration(BiConsumer<TTarget,TTargetParameter> setter,
                                            Function<TSource,TSourceParameter> getter,
                                            Function<TSourceParameter,TTargetParameter> converter) {
        this.setter = setter;
        this.getter = getter;
        this.converter = converter;
    }

    @Override
    public FieldMapper<TSource, TTarget> build() {
        BiConsumer<TSource, TTarget> fieldMapperLambda = (source, target) -> {
            setter.accept(target, converter.apply(getter.apply(source)));
        };

        return new FieldMapper<>(fieldMapperLambda);
    }

    @Override
    public BiConsumer<TTarget, ?> getSetter() {
        return this.setter;
    }
}
