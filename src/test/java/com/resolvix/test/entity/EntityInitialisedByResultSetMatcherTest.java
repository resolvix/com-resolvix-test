package com.resolvix.test.entity;

import com.resolvix.test.EntityMatchers;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.ResultSet;
import java.sql.SQLException;

public class EntityInitialisedByResultSetMatcherTest
    extends AbstractEntityMatcherTest
{

    private static A from(ResultSet resultSet)
        throws SQLException
    {
        A a = new A();
        a.primitiveIntegerValue = resultSet.getInt("primitiveIntegerValue");
        a.integerValue = resultSet.getInt("integerValue");
        a.stringValue = resultSet.getString("stringValue");
        a.primitiveBooleanValue = resultSet.getBoolean("primitiveBooleanValue");
        a.booleanValue = resultSet.getBoolean("booleanValue");
        return a;
    }

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void success() {

        Assert.assertThat(
            A.class,
            EntityMatchers.entityMatches(
                EntityMatchers.hasField("primitiveIntegerValue", int.class),
                EntityMatchers.hasField("integerValue", Integer.class),
                EntityMatchers.hasField("stringValue", String.class),
                EntityMatchers.hasField("primitiveBooleanValue", boolean.class),
                EntityMatchers.hasField("booleanValue", Boolean.class)
            ));
    }
}
