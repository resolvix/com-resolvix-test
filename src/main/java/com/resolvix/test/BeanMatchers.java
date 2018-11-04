package com.resolvix.test;

import com.resolvix.test.beans.BeanMatcher;
import com.resolvix.test.beans.BeanPropertyMatcher;
import org.hamcrest.Matcher;

public class BeanMatchers {

    public static <T> BeanMatcher<T> has(BeanPropertyMatcher<?>... beanPropertyMatchers) {
        return BeanMatcher.of(beanPropertyMatchers);
    }

    public static <T> BeanPropertyMatcher<T> property(String path, Matcher<?> valueMatcher) {
        return BeanPropertyMatcher.of(path, valueMatcher);
    }
}
