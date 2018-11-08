package com.resolvix.test.bean;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

public class BeanMatcherTest {

    @SuppressWarnings("unused")
    public class A {

        private Integer a;

        A(Integer a) {
            this.a = a;
        }

        public Integer getA() { return a; }
    }

    @SuppressWarnings("unused")
    public class B extends A {

        private Integer b;

        B(Integer a, Integer b) {
            super(a);
            this.b = b;
        }

        public Integer getB() { return b; }
    }

    @SuppressWarnings("unused")
    public class C extends B {

        private Integer c;

        C(Integer a, Integer b, Integer c) {
            super(a, b);
            this.c = c;
        }

        public Integer getC() { return c; }

        public Integer getD() {
            throw new RuntimeException();
        }

        public void setE(Integer e) { }

        Integer getF() {
            return 0;
        }
    }

    @SuppressWarnings("unused")
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

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void before() {
        this.objectA = new A(a);
        this.objectB = new B(a, b);
        this.objectC = new C(a, b, c);
        this.objectD = new D(objectA, objectB, objectC);
    }

    @Test
    public void BeanPropertyMatcher_success() {
        assertThat(objectA, HasProperty.of("a", equalTo(a)));
    }

    @Test
    public void BeanPropertyMatcher_success_inherited_properties() {
        assertThat(objectB, HasProperty.of("a", equalTo(a)));
        assertThat(objectC, HasProperty.of("a", equalTo(a)));
    }

    @Test
    public void BeanPropertyMatcher_failure_value_mismatch() {
        thrown.expect(AssertionError.class);
        assertThat(objectA, HasProperty.of( "a", equalTo(0)));
    }

    @Test
    public void BeanPropertyMatcher_failure_property_not_found() {
        thrown.expect(AssertionError.class);
        assertThat(objectA, HasProperty.of( "b", equalTo(0)));
    }

    @Test
    public void BeanPropertyMatcher_failure_property_not_readable() {
        thrown.expect(AssertionError.class);
        assertThat(objectC, HasProperty.of("e", equalTo(0)));
    }

    @Test
    public void BeanPropertyMatcher_failure_property_exception() {
        thrown.expect(AssertionError.class);
        assertThat(objectC, HasProperty.of( "d", equalTo(0)));
    }

    @Test
    public void ComposedBeanPropertyMatcher_success() {
        assertThat(objectD, HasProperty.of("a.a", equalTo(a)));
    }

    @Test
    public void ComposedBeanPropertyMatcher_success_inherited_properties() {
        assertThat(objectD, HasProperty.of("b.a", equalTo(a)));
        assertThat(objectD, HasProperty.of("c.a", equalTo(a)));
    }

    @Test
    public void ComposedBeanPropertyMatcher_failure_value_mismatch() {
        thrown.expect(AssertionError.class);
        assertThat(objectD, HasProperty.of("c.a", equalTo(0)));
    }

    @Test
    public void ComposedBeanPropertyMatcher_failure_property_not_found() {
        thrown.expect(AssertionError.class);
        assertThat(objectD, HasProperty.of( "c.g", equalTo(0)));
    }

    @Test
    public void ComposedBeanPropertyMatcher_failure_property_not_readable() {
        thrown.expect(AssertionError.class);
        assertThat(objectD, HasProperty.of("c.e", equalTo(0)));
    }

    @Test
    public void ComposedBeanPropertyMatcher_failure_property_exception() {
        thrown.expect(AssertionError.class);
        assertThat(objectD, HasProperty.of( "c.d", equalTo(0)));
    }

    @Test
    public void ComposedBeanPropertyMatcher_failure_property_not_accessible() {
        thrown.expect(AssertionError.class);
        assertThat(objectD, HasProperty.of( "c.f", equalTo(0)));
    }

    @Test
    public void BeanMatcher_success() {
        assertThat(objectD, BeanMatcher.of(
            HasProperty.of( "a.a", equalTo(1)),
            HasProperty.of("b.b", equalTo(2)),
            HasProperty.of("c.c", equalTo(3))
        ));
    }

    @Test
    public void BeanMatcher_failure() {
        thrown.expect(AssertionError.class);
        assertThat(objectD, BeanMatcher.of(
            HasProperty.of("a.a", equalTo(1)),
            HasProperty.of("c.a", equalTo(0)),
            HasProperty.of("c.b", equalTo(0))
        ));
    }
}
