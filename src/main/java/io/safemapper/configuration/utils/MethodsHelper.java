package io.safemapper.configuration.utils;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MethodsHelper<T> {
    private final Class<T> targetClass;

    public MethodsHelper(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public List<Method> getSetters() {
        return Arrays.stream(targetClass.getMethods())
                .filter(m -> m.getName().startsWith("set"))
                .filter(m -> m.getParameterCount() == 1)
                .collect(Collectors.toList());
    }

    public List<Method> getGetters() {
        return Arrays.stream(targetClass.getMethods())
                .filter(m -> m.getName().startsWith("get"))
                .filter(m -> m.getParameterCount() == 0)
                .collect(Collectors.toList());
    }
}
