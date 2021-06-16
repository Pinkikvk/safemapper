package io.safemapper.configuration.utils;

public class DefaultFactory {
    public static Object getDefaultForType(Class<?> type) {
        if (type.equals(Integer.class)) {
            return 0;
        }

        if (type.equals(Byte.class)) {
            return (byte) 0;
        }

        if (type.equals(Double.class)) {
            return 0d;
        }

        if (type.equals(Boolean.class)) {
            return false;
        }

        if (type.equals(Short.class)) {
            return (short) 0;
        }

        if (type.equals(Character.class)) {
            return '\u0000';
        }

        if (type.equals(Long.class)) {
            return 0L;
        }

        return null;
    }
}
