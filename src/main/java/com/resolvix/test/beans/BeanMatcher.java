package com.resolvix.test.beans;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class BeanMatcher<T> extends BaseMatcher<T> {

    private static final String MISMATCH_SEPARATOR = "; ";

    private BeanPropertyMatcher[] propertyMatchers;
    private Description expectedDescription;
    private Description mismatchDescription;

    public BeanMatcher(BeanPropertyMatcher<?>... propertyMatchers) {
        this.propertyMatchers = propertyMatchers;
    }

    private void appendMatcherDescriptions(Object item, Matcher<?> matcher) {
        matcher.describeTo(expectedDescription);
        matcher.describeMismatch(item, mismatchDescription);
        mismatchDescription.appendText(MISMATCH_SEPARATOR);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(expectedDescription.toString());
    }

    @Override
    public void describeMismatch(Object item, Description description) {
        description.appendText(mismatchDescription.toString());
    }

    @Override
    public boolean matches(Object item) {
        boolean matches = true;
        for (BeanPropertyMatcher<?> propertyMatcher : propertyMatchers) {
            if (!propertyMatcher.matches(item)) {
                matches = false;
                appendMatcherDescriptions(item, propertyMatcher);
            }
        }
        return matches;
    }
}
