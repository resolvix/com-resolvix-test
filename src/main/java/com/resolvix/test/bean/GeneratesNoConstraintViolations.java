package com.resolvix.test.bean;

import com.resolvix.test.bean.base.AbstractBeanValidationConstraintViolationMatcher;
import org.hamcrest.Description;

public class GeneratesNoConstraintViolations<T>
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
