package com.resolvix.test;

import org.hamcrest.Matcher;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.isA;
import static org.hamcrest.core.CombinableMatcher.both;

public class Matchers {

    public static <T> Matcher<T> ofTypeAndValue(Class<T> type, T t) {
        return both(equalTo(t))
                .and(isA(type));
    }
}
