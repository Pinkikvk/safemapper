package io.safemapper.configuration.utils;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.javatuples.Pair;

import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

public class SetterDetector<T> {
    private Class<T> targetClass;

    public SetterDetector(Class<T> targetClass) {
        this.targetClass = targetClass;
    }

    private T buildEnhancer(List<Method> executedMethods) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
            if (method.getDeclaringClass() != Object.class) {
                executedMethods.add(method);
            }
            return null;
        });

        return (T) enhancer.create();
    }

    private <U> void runLambdaUsingEnhancer(BiConsumer<T, U> lambda, T enhancer) {
        try {
            lambda.accept(enhancer, null);
        }
        catch (Exception ex) {

        }
    }

    public <U> Method matchSetter(BiConsumer<T, U> lambda) {
        var executedMethods = new LinkedList<Method>();

        T enhancer = buildEnhancer(executedMethods);
        runLambdaUsingEnhancer(lambda, enhancer);

        if (executedMethods.size() == 1) {
            return executedMethods.get(0);
        }

        return null;
    }
}
