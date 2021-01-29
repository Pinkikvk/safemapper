package io.safemapper.mapper;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class FieldMapper<TSource, TTarget> {

    private final BiConsumer<TSource, TTarget> mappingLambda;

    public FieldMapper(BiConsumer<TSource, TTarget> mappingLambda) {
        this.mappingLambda = mappingLambda;
    }

    public void execute(TSource source, TTarget target) {
        mappingLambda.accept(source, target);
    }
}
