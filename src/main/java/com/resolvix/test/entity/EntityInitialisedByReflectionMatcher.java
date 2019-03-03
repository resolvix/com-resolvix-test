package com.resolvix.test.entity;

import org.hamcrest.Description;

public class EntityInitialisedByReflectionMatcher<T>
    extends AbstractEntityMatcher<T>
    implements EntityMatcher<T> {


    @Override
    public boolean matches(Object o) {
        return false;
    }

    @Override
    public void describeMismatch(Object o, Description description) {

    }

    @Override
    public void describeTo(Description description) {

    }
}
