package com.resolvix.test.entity;

import java.util.function.Function;

public class SpecifiedValueEntityField<S, T>
    extends AbstractEntityField<S, T>
    implements EntityField<S, T> {

    private T valueT;

    private SpecifiedValueEntityField(
        String fieldName, Class<T> classT, T valueT, Function<S, T> getter) {
        super(fieldName, classT, getter);
        this.valueT = valueT;
    }

    public static <S, T> SpecifiedValueEntityField<S, T> of(
        String fieldName, Class<T> classT, T valueT, Function<S, T> getter) {
        return new SpecifiedValueEntityField<>(
            fieldName, classT, valueT, getter);
    }
}
