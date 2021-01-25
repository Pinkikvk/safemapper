package io.safemapper.configuration;

import io.safemapper.configuration.utils.MethodsHelper;
import io.safemapper.configuration.utils.SetterDetector;
import io.safemapper.mapper.FieldMapper;
import io.safemapper.mapper.Mapper;

import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MapperConfiguration<TSource, TTarget> {
    private final Class<TSource> sourceClass;
    private final Class<TTarget> destinationClass;
    private final SetterDetector<TTarget> setterDetector;
    private final MethodsHelper<TTarget> methodsHelper;
    private final List<FieldMapper<TSource, TTarget>> fieldMappers = new LinkedList<>();

    private MapperConfiguration(Class<TSource> sourceClass, Class<TTarget> destinationClass) {
        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;
        setterDetector = new SetterDetector<>(destinationClass);
        methodsHelper = new MethodsHelper<>(destinationClass);
    }

    public static <TSource,TTarget> MapperConfiguration<TSource,TTarget> create(Class<TSource> sourceClass, Class<TTarget> destinationClass) {
        return new MapperConfiguration<>(sourceClass, destinationClass);
    }

    public Mapper<TSource, TTarget> build() {
        return new Mapper<>(fieldMappers);
    }

    public <W> MapperConfiguration<TSource, TTarget> ignore(BiConsumer<TTarget,W> setter) {
        //var setters = methodsHelper.getSetters();

        //Method matchedSetterMethod = setterDetector.matchSetter(setter);
        return this;
    }

    public <W> MapperConfiguration<TSource, TTarget> addMapping(BiConsumer<TTarget,W> setter, Function<TSource,W> getter) {
        fieldMappers.add(FieldMapper.build(setter, getter));
        return this;
    }

    public <W,X> MapperConfiguration<TSource, TTarget> addMapping(BiConsumer<TTarget,W> setter, Function<TSource,X> getter, Function<X,W> converter) {
        fieldMappers.add(FieldMapper.build(setter, getter, converter));
        return this;
    }
}