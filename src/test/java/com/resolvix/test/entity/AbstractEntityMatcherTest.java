package com.resolvix.test.entity;

public abstract class AbstractEntityMatcherTest {

    protected static class A {

        protected int primitiveIntegerValue;

        protected Integer integerValue;

        protected String stringValue;

        protected boolean primitiveBooleanValue;

        protected Boolean booleanValue;

        public int getPrimitiveIntegerValue() {
            return primitiveIntegerValue;
        }

        public Integer getIntegerValue() {
            return integerValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public boolean isPrimitiveBooleanValue() {
            return primitiveBooleanValue;
        }

        public Boolean getBooleanValue() {
            return booleanValue;
        }
    }
}
