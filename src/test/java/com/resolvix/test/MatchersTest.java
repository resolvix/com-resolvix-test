package com.resolvix.test;

import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static com.resolvix.test.Matchers.ofTypeAndValue;

public class MatchersTest {

    @Test
    public void primitiveTypeAndValueTest() {
        int actual = 1;
        assertThat(actual, equalTo(1));
        assertThat(actual, ofTypeAndValue(int.class, 1));
        assertThat(actual, ofTypeAndValue(Integer.class, 1));
    }

    @Test
    public void nonPrimitiveTypeAndValueTest() {
        Integer actual = 1;
        assertThat(actual, ofTypeAndValue(int.class,1));
        assertThat(actual, ofTypeAndValue(Integer.class, 1));
    }
}
