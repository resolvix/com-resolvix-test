package com.resolvix.test.bean;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class BeanMatcher<T> extends BaseMatcher<T> {

    private static final String MISMATCH_SEPARATOR = "; ";

    private HasProperty[] propertyMatchers;
    private Description expectedDescription = new StringDescription();
    private Description mismatchDescription = new StringDescription();

    private BeanMatcher(HasProperty<?>... propertyMatchers) {
        this.propertyMatchers = propertyMatchers;
    }

    public static <T> BeanMatcher<T> of(HasProperty<?>... propertyMatchers) {
        return new BeanMatcher<>(propertyMatchers);
    }

    private void appendMatcherDescriptions(Object item, Matcher<?> matcher) {
        matcher.describeTo(expectedDescription);
        expectedDescription.appendText(MISMATCH_SEPARATOR);
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
        for (HasProperty<?> propertyMatcher : propertyMatchers) {
            if (!propertyMatcher.matches(item)) {
                matches = false;
                appendMatcherDescriptions(item, propertyMatcher);
            }
        }
        return matches;
    }
}
