package com.resolvix.test.entity;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

public class EntityInitialisedByResultSetMatcher<T>
    extends BaseMatcher<T>
    implements EntityMatcher<T>
{

    private EntityField<T, ?>[] entityFields;

    private EntityInitialisedByResultSetMatcher(EntityField<T, ?>... entityFields) {
        this.entityFields = entityFields;
    }

    public static <T> EntityInitialisedByResultSetMatcher<T> of(EntityField<T, ?>... entityFields) {
        return new EntityInitialisedByResultSetMatcher<T>(entityFields);
    }

    @Override
    public boolean matches(Object o) {
        return true;
    }

    @Override
    public void describeTo(Description description) {

    }
}
