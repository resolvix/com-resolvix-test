package com.resolvix.test.entity;

import java.util.function.Function;

public abstract class AbstractEntityField<S, T> {

    private String fieldName;

    private Class<T> classT;

    private T valueT;

    private Function<S, T> getter;

    protected AbstractEntityField(
        String fieldName, Class<T> classT, Function<S, T> getter) {
        this.fieldName = fieldName;
        this.classT = classT;
        this.getter = getter;
    }
}
