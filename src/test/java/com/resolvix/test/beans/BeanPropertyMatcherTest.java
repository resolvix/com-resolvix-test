package com.resolvix.test.beans;

import com.resolvix.lib.reflect.BeanUtils;
import junit.framework.AssertionFailedError;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;

import java.beans.PropertyDescriptor;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class BeanPropertyMatcherTest {

    public class A {

        private Integer a;

        A(Integer a) {
            this.a = a;
        }

        public Integer getA() { return a; }
    }

    public class B extends A {

        private Integer b;

        B(Integer a, Integer b) {
            super(a);
            this.b = b;
        }

        public Integer getB() { return b; }
    }

    public class C extends B {

        private Integer c;

        C(Integer a, Integer b, Integer c) {
            super(a, b);
            this.c = c;
        }

        public Integer getC() { return c; }
    }

    public class D {

        private A a;
        private B b;
        private C c;

        D(A a, B b, C c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public A getA() { return a; }

        public B getB() { return b; }

        public C getC() { return c; }

    }

    private Integer a = 1;
    private Integer b = 2;
    private Integer c = 3;

    private A objectA;

    private B objectB;

    private C objectC;

    private D objectD;

    @Before
    public void before() {
        this.objectA = new A(a);
        this.objectB = new B(a, b);
        this.objectC = new C(a, b, c);
        this.objectD = new D(objectA, objectB, objectC);
    }

    @Test
    public void BeanPropertyMatcher_success() {
        assertThat(objectA, BeanPropertyMatcher.of("a", equalTo(a)));
    }

    public void BeanPropertyMatcher_success_inherited_properties() {
        assertThat(objectB, BeanPropertyMatcher.of("a", equalTo(a)));
        assertThat(objectC, BeanPropertyMatcher.of("a", equalTo(a)));
    }

    @Test(expected = AssertionError.class)
    public void BeanPropertyMatcher_failure_value_mismatch() {
        assertThat(objectA, BeanPropertyMatcher.of( "a", equalTo(0)));
    }

    @Test(expected = AssertionError.class)
    public void BeanPropertyMatcher_failure_property_not_found() {
        assertThat(objectA, BeanPropertyMatcher.of( "b", equalTo(0)));
    }

    @Test
    public void ComposedBeanPropertyMatcher_success() {
        assertThat(objectD, BeanPropertyMatcher.of("a.a", equalTo(a)));
    }

    @Test
    public void ComposedBeanPropertyMatcher_success_inherited_properties() {
        assertThat(objectD, BeanPropertyMatcher.of("b.a", equalTo(a)));
        assertThat(objectD, BeanPropertyMatcher.of("c.a", equalTo(a)));
    }

    @Test(expected = AssertionError.class)
    public void ComposedBeanPropertyMatcher_failure_value_mismatch() {
        assertThat(objectD, BeanPropertyMatcher.of("c.a", equalTo(0)));
    }

    @Test(expected = AssertionError.class)
    public void ComposedBeanPropertyMatcher_failure_property_not_found() {
        assertThat(objectA, BeanPropertyMatcher.of( "c.d", equalTo(0)));
    }
}
