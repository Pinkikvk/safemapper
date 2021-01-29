package io.safemapper.mapper;

import java.util.List;

public class Mapper<TSource,TTarget> {

    private final List<FieldMapper<TSource, TTarget>> fieldMappers;

    public Mapper(List<FieldMapper<TSource, TTarget>> fieldMappers) {
        this.fieldMappers = fieldMappers;
    }

    public TTarget map(TSource source) {
        return null;
    }

    public TTarget map(TSource source, TTarget target) {
        fieldMappers.stream()
                .forEach(m -> m.execute(source, target));

        return target;
    }

}

