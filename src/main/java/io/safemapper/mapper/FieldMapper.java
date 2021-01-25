package io.safemapper.mapper;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class FieldMapper<TSource, TTarget> {

    private final BiConsumer<TSource, TTarget> mappingLambda;

    private FieldMapper(BiConsumer<TSource, TTarget> mappingLambda) {
        this.mappingLambda = mappingLambda;
    }

    public void execute(TSource source, TTarget target) {
        mappingLambda.accept(source, target);
    }

    public static <TSource, TTarget, W> FieldMapper<TSource, TTarget> build(BiConsumer<TTarget,W> setter,
                                                                            Function<TSource,W> getter) {

        BiConsumer<TSource, TTarget> mappingLambda = (source, target) -> setter.accept(target, getter.apply(source));
        return new FieldMapper<>(mappingLambda);
    }

    public static <TSource, TTarget, W, X> FieldMapper<TSource, TTarget> build(BiConsumer<TTarget,W> setter,
                                                                            Function<TSource,X> getter,
                                                                            Function<X,W> converter) {

        BiConsumer<TSource, TTarget> mappingLambda = (source, target) -> setter.accept(target, converter.apply(getter.apply(source)));
        return new FieldMapper<>(mappingLambda);
    }

}
