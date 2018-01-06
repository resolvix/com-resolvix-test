package com.resolvix.test.beans;

import org.hamcrest.Description;

public class GeneratesConstraintViolations<T>
    extends AbstractBeanValidationConstraintViolationMatcher<T>
{
    @Override
    public boolean matches(Object o) {
        return false;
    }

    @Override
    public void describeTo(Description description) {

    }
}
