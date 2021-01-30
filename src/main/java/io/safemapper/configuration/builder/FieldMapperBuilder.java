package io.safemapper.configuration.builder;

import io.safemapper.mapper.FieldMapper;

import java.util.function.BiConsumer;
import java.util.function.Function;

public abstract class FieldMapperBuilder {
    static <TSource, TTarget, W> FieldMapper<TSource, TTarget> build(BiConsumer<TTarget,W> setter,
                                                                     Function<TSource,W> getter) {

        BiConsumer<TSource, TTarget> mappingLambda = (source, target) -> setter.accept(target, getter.apply(source));
        return new FieldMapper<>(mappingLambda);
    }

    static <TSource, TTarget, W, X> FieldMapper<TSource, TTarget> build(BiConsumer<TTarget,W> setter,
                                                                               Function<TSource,X> getter,
                                                                               Function<X,W> converter) {

        BiConsumer<TSource, TTarget> mappingLambda = (source, target) -> setter.accept(target, converter.apply(getter.apply(source)));
        return new FieldMapper<>(mappingLambda);
    }
}
