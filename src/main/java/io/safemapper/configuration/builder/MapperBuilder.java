package io.safemapper.configuration.builder;

import io.safemapper.configuration.FieldMappingConfiguration;
import io.safemapper.configuration.MappingConfiguration;
import io.safemapper.configuration.utils.MethodsHelper;
import io.safemapper.configuration.utils.SetterDetector;
import io.safemapper.exception.MapperException;
import io.safemapper.mapper.FieldMapper;
import io.safemapper.mapper.Mapper;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MapperBuilder<TSource, TTarget> {

    private final MappingConfiguration<TSource, TTarget> mappingConfiguration;
    private final MethodsHelper<TTarget> methodsHelper;
    private final SetterDetector<TTarget> setterDetector;

    private MapperBuilder(MappingConfiguration<TSource, TTarget> mappingConfiguration) {
        this.mappingConfiguration = mappingConfiguration;
        this.methodsHelper = new MethodsHelper<>(mappingConfiguration.getTargetClass());
        this.setterDetector = new SetterDetector<>(mappingConfiguration.getTargetClass());
    }

    public static <TSource, TTarget> MapperBuilder<TSource, TTarget> of(MappingConfiguration<TSource, TTarget> mappingConfiguration) {
        return new MapperBuilder<>(mappingConfiguration);
    }

    public Mapper<TSource, TTarget> build() {
        Map<Method, FieldMapper<TSource, TTarget>> fieldMap = buildFieldsMap();
        applyUserDefinedMappings(fieldMap);

        if (!mappingConfiguration.areMissingFieldsAllowed()) {
            verifyMissingConfiguration(fieldMap);
        }

        return buildMapper(fieldMap);
    }

    private Map<Method, FieldMapper<TSource, TTarget>> buildFieldsMap() {
        var fieldsMap = new HashMap<Method, FieldMapper<TSource, TTarget>>();

        for (var method: methodsHelper.getSetters()) {
            fieldsMap.put(method, null);
        }

        return fieldsMap;
    }

    private void applyUserDefinedMappings(Map<Method, FieldMapper<TSource, TTarget>> fieldMap) {
        for (var fieldMappingConfiguration: mappingConfiguration.getFieldMappingConfigurations()) {
            applyUserDefinedMapping(fieldMap, fieldMappingConfiguration);
        }
    }

    private void applyUserDefinedMapping(Map<Method, FieldMapper<TSource, TTarget>> fieldMap,
                                         FieldMappingConfiguration<TSource, TTarget> fieldMappingConfiguration) {

        var setterLambda = fieldMappingConfiguration.getSetter();
        Method setter = setterDetector.matchSetter(setterLambda)
                .orElseThrow(() -> new MapperException("Cannot find setter for provided lambda"));

        if (!fieldMap.containsKey(setter)) {
            throw new MapperException(String.format("Provided lambda detected as method <%s()> but not as a setter", setter.getName()));
        }

        if (fieldMap.get(setter) != null) {
            throw new MapperException(String.format("Mapping for <%s()> is defined twice", setter.getName()));
        }

        fieldMap.replace(setter, fieldMappingConfiguration.build());
    }

    private void verifyMissingConfiguration(Map<Method, FieldMapper<TSource, TTarget>> fieldMap) {
        var missedSetters = fieldMap.entrySet()
                .stream()
                .filter(e -> e.getValue() == null)
                .map(Map.Entry::getKey)
                .map(Method::getName)
                .collect(Collectors.toList());

        if (!missedSetters.isEmpty()) {
            var missedSettersNames = String.join(",", missedSetters);

            throw new MapperException(String.format("Missing configuration for: %s", missedSettersNames));
        }
    }

    private Mapper<TSource, TTarget> buildMapper(Map<Method, FieldMapper<TSource, TTarget>> fieldMap) {
        List<FieldMapper<TSource, TTarget>> fieldMappers = fieldMap
                .values()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return new Mapper<>(fieldMappers);
    }
}
