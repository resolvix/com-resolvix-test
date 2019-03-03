package com.resolvix.test;

import com.resolvix.test.entity.*;

import java.util.function.Function;

public class EntityMatchers {

    public static <T> EntityMatcher<T> entityMatches(EntityField<T, ?>... entityFields) {
        return EntityInitialisedByResultSetMatcher.of(entityFields);
    }

    public static <S, T> EntityField<S, T> hasField(
        String fieldName, Class<T> classT, T valueT, Function<S, T> getter) {
        return SpecifiedValueEntityField.of(
            fieldName, classT, valueT, getter);
    }

    public static <S, T> EntityField<S, T> hasField(
        String fieldName, Class<T> classT) {
        return AutoGeneratedValueEntityField.of(
            fieldName, classT);
    }
}