package io.safemapper.mapper;

import io.safemapper.configuration.MapperConfiguration;

public class Mapper<S,T> {

    private final MapperConfiguration<S,T> configuration;

    public Mapper(MapperConfiguration<S,T> configuration) {
        this.configuration = configuration;
    }

    public T map(S source) {
        return null;
    }

    public T map(S source, T target) {
        return target;
    }

}

