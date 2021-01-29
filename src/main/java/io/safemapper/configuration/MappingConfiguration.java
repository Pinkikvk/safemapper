package io.safemapper.configuration;

import io.safemapper.configuration.utils.MethodsHelper;
import io.safemapper.configuration.utils.SetterDetector;
import io.safemapper.mapper.FieldMapper;
import io.safemapper.mapper.Mapper;

import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MappingConfiguration<TSource, TTarget> {
    private final Class<TSource> sourceClass;
    private final Class<TTarget> destinationClass;
    private final SetterDetector<TTarget> setterDetector;
    private final MethodsHelper<TTarget> methodsHelper;
    private final List<FieldMappingConfiguration<TSource, TTarget>> fieldMappingConfigurations = new LinkedList<>();

    private MappingConfiguration(Class<TSource> sourceClass, Class<TTarget> destinationClass) {
        this.sourceClass = sourceClass;
        this.destinationClass = destinationClass;
        setterDetector = new SetterDetector<>(destinationClass);
        methodsHelper = new MethodsHelper<>(destinationClass);
    }

    public static <TSource,TTarget> MappingConfiguration<TSource,TTarget> create(Class<TSource> sourceClass, Class<TTarget> destinationClass) {
        return new MappingConfiguration<>(sourceClass, destinationClass);
    }

    public Mapper<TSource, TTarget> build() {
        List<FieldMapper<TSource, TTarget>> fieldMappers = fieldMappingConfigurations
                .stream()
                .map(FieldMappingConfiguration::build)
                .collect(Collectors.toList());

        return new Mapper<>(fieldMappers);
    }

    public <W> MappingConfiguration<TSource, TTarget> ignore(BiConsumer<TTarget,W> setter) {
        var fieldMappingConfiguration = new IgnoreFieldMappingConfiguration<TSource, TTarget>();
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

    private void addMappingConfiguration(FieldMappingConfiguration<TSource, TTarget> fieldMappingConfiguration) {
        fieldMappingConfigurations.add(fieldMappingConfiguration);
    }
}