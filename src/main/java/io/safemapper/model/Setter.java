package io.safemapper.model;

@FunctionalInterface
public interface Setter<TargetType, ValueType> {
    void set(TargetType obj, ValueType value);
}
