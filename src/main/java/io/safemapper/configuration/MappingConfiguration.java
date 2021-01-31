package io.safemapper.configuration;

import io.safemapper.configuration.builder.MapperBuilder;
import io.safemapper.configuration.utils.SetterDetector;
import io.safemapper.exception.MapperException;
import io.safemapper.mapper.Mapper;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class MappingConfiguration<TSource, TTarget> {
    private final Class<TSource> sourceClass;
    private final Class<TTarget> targetClass;
    private final List<FieldMappingConfiguration<TSource, TTarget>> fieldMappingConfigurations = new LinkedList<>();
    private final SetterDetector<TTarget> setterDetector;

    private boolean areMissingFieldsAllowed = false;

    private MappingConfiguration(Class<TSource> sourceClass, Class<TTarget> targetClass) {
        this.sourceClass = sourceClass;
        this.targetClass = targetClass;
        this.setterDetector = new SetterDetector<>(targetClass);
    }

    public static <TSource,TTarget> MappingConfiguration<TSource,TTarget> create(Class<TSource> sourceClass, Class<TTarget> destinationClass) {
        return new MappingConfiguration<>(sourceClass, destinationClass);
    }

    public Mapper<TSource, TTarget> build() {
        var mapperBuilder = MapperBuilder.of(this);
        return mapperBuilder.build();
    }

    public <W> MappingConfiguration<TSource, TTarget> ignore(BiConsumer<TTarget,W> setter) {
        var fieldMappingConfiguration = new IgnoreFieldMappingConfiguration<TSource, TTarget>(setter);
        addMappingConfiguration(fieldMappingConfiguration);
        return this;
    }

    public <W> MappingConfiguration<TSource, TTarget> addMapping(BiConsumer<TTarget,W> setter, Function<TSource,W> getter) {
        var fieldMappingConfiguration = new BasicFieldMappingConfiguration<>(setter, getter);
        addMappingConfiguration(fieldMappingConfiguration);
        return this;
    }

    public <W,X> MappingConfiguration<TSource, TTarget> addMapping(BiConsumer<TTarget,W> setter, Function<TSource,X> getter, Function<X,W> converter) {
        var fieldMappingConfiguration = new ConvertFieldMappingConfiguration<>(setter, getter, converter);
        addMappingConfiguration(fieldMappingConfiguration);
        return this;
    }

    public List<FieldMappingConfiguration<TSource, TTarget>> getFieldMappingConfigurations() {
        return fieldMappingConfigurations;
    }

    public Class<TSource> getSourceClass() {
        return this.sourceClass;
    }

    public Class<TTarget> getTargetClass() {
        return this.targetClass;
    }

    public MappingConfiguration<TSource, TTarget> missingFieldsAllowed() {
        this.areMissingFieldsAllowed = true;
        return this;
    }

    public boolean areMissingFieldsAllowed() {
        return this.areMissingFieldsAllowed;
    }

    private void addMappingConfiguration(FieldMappingConfiguration<TSource, TTarget> fieldMappingConfiguration) {
        var setterLambda = fieldMappingConfiguration.getSetter();
        setterDetector.findSetterMethod(setterLambda).orElseThrow(() ->
            new MapperException(String.format("Provided setter lambda not recognized as %s method", targetClass.getSimpleName()))
        );

        fieldMappingConfigurations.add(fieldMappingConfiguration);
    }
}