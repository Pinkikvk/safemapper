package io.safemapper.configuration.utils;

import io.safemapper.model.Setter;
import io.safemapper.model.TestClass;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;

public class SetterDetectorTest {

    @Test
    public void findRefTypeSetter() throws NoSuchMethodException {
        var setterDetector = new SetterDetector<>(TestClass.class);
        Setter<TestClass, String> setterLambda = TestClass::setString;
        var setterMethod = TestClass.class.getMethod("setString", String.class);

        var detectedSetterMethod = setterDetector.findSetterMethod(setterLambda).get();

        Assertions.assertEquals(setterMethod, detectedSetterMethod);
    }

    @Test
    public void findValueTypeSetter() throws NoSuchMethodException {
        var setterDetector = new SetterDetector<>(TestClass.class);
        Setter<TestClass, Integer> setterLambda = TestClass::setInt;

        var setterMethod = TestClass.class.getMethod("setInt", int.class);

        var detectedSetterMethod = setterDetector.findSetterMethod(setterLambda).get();

        Assertions.assertEquals(setterMethod, detectedSetterMethod);
    }

}
