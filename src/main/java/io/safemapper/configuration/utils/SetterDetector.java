package io.safemapper.configuration.utils;

import io.safemapper.model.Setter;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.matcher.ElementMatchers;
import net.jodah.typetools.TypeResolver;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class SetterDetector<T> {

    public class GeneralInterceptor {

        public List<Method> executedMethods = new LinkedList<>();

        @RuntimeType
        public Object intercept(@Origin Method method) {
            executedMethods.add(method);
            return null;
        }
    }

    private final Class<T> targetClass;

    public SetterDetector(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    public Optional<Method> findSetterMethod(Setter<T, ?> lambda) {
        var interceptor = new GeneralInterceptor();

        T enhancer = buildEnhancer(interceptor);
        runLambdaUsingEnhancer(lambda, enhancer);

        if (interceptor.executedMethods.size() == 1) {
            return Optional.of(interceptor.executedMethods.get(0));
        }

        return Optional.empty();
    }

    private T buildEnhancer(GeneralInterceptor interceptor) {
        try {
            return new ByteBuddy()
                    .subclass(targetClass)
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(interceptor))
                    .make()
                    .load(getClass().getClassLoader())
                    .getLoaded()
                    .getConstructor(null)
                    .newInstance();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    private void runLambdaUsingEnhancer(Setter<T, ?> lambda, T enhancer) {
        Object defaultArgumentValue = getDefaultArgumentValue(lambda);
        Setter<T, Object> castedLambda = (Setter<T, Object>) lambda;
        castedLambda.set(enhancer, defaultArgumentValue);
    }

    private Object getDefaultArgumentValue(Setter<T, ?> lambda) {
        Class<?>[] typeArgs = TypeResolver.resolveRawArguments(BiConsumer.class, lambda.getClass());

        //When setter is assigned to BiConsumer, and setter argument type is primitive, it is boxed
        //Information if original type is primitive or reference type is lost
        //For setter "test" invoke, we need any argument. We cannot use null, cos for primitive it will cause runtime exception
        //For type that might be boxed, eg Integer we will use boxed value of primitive default value. In this case Integer.valueOf(0).

        Class<?> argumentType = typeArgs[1];
        return getDefaultForType(argumentType);
    }

    private Object getDefaultForType(Class<?> type) {
        if (type.equals(Integer.class)) {
            return Integer.valueOf(0);
        }

        if (type.equals(Byte.class)) {
            return Byte.valueOf((byte)0);
        }

        if (type.equals(Double.class)) {
            return Double.valueOf(0d);
        }

        if (type.equals(Boolean.class)) {
            return Boolean.FALSE;
        }

        if (type.equals(Short.class)) {
            return Short.valueOf((short)0);
        }

        if (type.equals(Character.class)) {
            return Character.valueOf('\u0000');
        }

        if (type.equals(Long.class)) {
            return Long.valueOf(0L);
        }

        return null;
    }

}
