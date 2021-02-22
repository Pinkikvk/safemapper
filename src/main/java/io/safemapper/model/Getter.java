package io.safemapper.model;

@FunctionalInterface
public interface Getter<SourceType, ValueType> {
    ValueType get(SourceType obj);
}
