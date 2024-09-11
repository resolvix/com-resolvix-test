package com.resolvix.test;

import org.hamcrest.MatcherAssert;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

public class CompositeMatcherTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private static class Subject
    {

        private final Integer id;

        private final String name;

        public Subject(Integer id, String name) {
            this.id = id;
            this.name = name;
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }

    public static class TestMatcher
        extends CompositeMatcher<Subject> {
        TestMatcher(Integer id, String name) {
            super(
                Arrays.<MatcherFunction<?>>asList(
                (MatcherFunction<?>) new GetterMatcher<Subject, Integer>("id", Subject::getId, equalTo(id)),
                (MatcherFunction<?>) new GetterMatcher<Subject, String>("name", Subject::getName, equalTo(name))));
        }
    }

    private Integer id = 99;

    private String name = "Object";

    private Subject subject;

    @Before
    public void before() {
        this.subject = new Subject(id, name);
    }

    @Test
    public void testSuccess() {
        assertThat(
            this.subject,
            new TestMatcher(99, "Object"));
    }

    @Test
    public void testMismatchOnId() {
        thrown.expect(AssertionError.class);
        assertThat(
            this.subject,
            new TestMatcher(98, "Object"));
    }

    @Test
    public void testMismatchOnName() {
        thrown.expect(AssertionError.class);
        assertThat(
            this.subject,
            new TestMatcher(99, "MismatchedObject"));
    }

    @Test
    public void testMismatchOnIdNull() {
        thrown.expect(AssertionError.class);
        assertThat(
            this.subject,
            new TestMatcher(null, "Object"));
    }

    @Test
    public void testMismatchOnNameNull() {
        thrown.expect(AssertionError.class);
        assertThat(
            this.subject,
            new TestMatcher(99, null));
    }

    //
    //  Multivariate mismatch
    //

    @Test

    public void testMismatchOnIdName() {
        thrown.expect(AssertionError.class);
        assertThat(
            this.subject,
            new TestMatcher(98, "MismatchedObject"));
    }

    @Test
    public void testMismatchOnIdNameBothNull() {
        thrown.expect(AssertionError.class);
        assertThat(
            this.subject,
            new TestMatcher(null, null));
    }

    @Test
    public void testMismatchOnSubjectIdNameNull() {
        thrown.expect(AssertionError.class);
        subject = new Subject(null, null);
        assertThat(
            subject,
            new TestMatcher(99, "Object"));
    }
}
