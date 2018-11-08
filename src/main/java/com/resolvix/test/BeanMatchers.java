package com.resolvix.test;

import com.resolvix.test.bean.BeanMatcher;
import com.resolvix.test.bean.HasProperty;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matcher;

public class BeanMatchers {

    public static <T> BeanMatcher<T> has(HasProperty<?>... hasProperties) {
        return BeanMatcher.of(hasProperties);
    }

    public static <T> HasProperty<T> hasProperty(String path, Matcher<?> valueMatcher) {
        return HasProperty.of(path, valueMatcher);
    }

    public static <T> Matcher<?> ofTypeAndValue(Class<?> type, T t) {
        return CoreMatchers.both(CoreMatchers.equalTo(type))
            .and(CoreMatchers.equalTo(t));
    }
}
