package com.resolvix.test.beans;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class BeanPropertyMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    @Override
    protected boolean matchesSafely(T t, Description description) {
        return false;
    }

    @Override
    public void describeTo(Description description) {

    }
}
