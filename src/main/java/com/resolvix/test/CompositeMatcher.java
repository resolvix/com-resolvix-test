package com.resolvix.test;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.SelfDescribing;
import org.hamcrest.StringDescription;
import org.hamcrest.TypeSafeMatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public abstract class CompositeMatcher<T>
    extends TypeSafeMatcher<T>
{
    interface MatcherFunction<T>
        extends Matcher<T>
    {

    }

    public static class GetterMatcher<C, V>
        extends TypeSafeMatcher<C>
        implements MatcherFunction<C>
    {
        private final String identifier;

        private final Function<C, V> getter;

        private final Matcher<V> valueMatcher;

        public GetterMatcher(String identifier, Function<C, V> getter, Matcher<V> valueMatcher) {
            this.identifier = identifier;
            this.getter = getter;
            this.valueMatcher = valueMatcher;
        }

        @Override
        protected boolean matchesSafely(C container) {
            return valueMatcher.matches(getter.apply(container));
        }

        @Override
        protected void describeMismatchSafely(C container, Description mismatchDescription) {
            mismatchDescription.appendText(identifier);
            mismatchDescription.appendText(" ");
            valueMatcher.describeMismatch(getter.apply(container), mismatchDescription);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(identifier);
            description.appendText(" ");
            valueMatcher.describeTo(description);
        }
    }

    private final List<MatcherFunction<?>> matcherFunctions;

    protected CompositeMatcher() {
        this.matcherFunctions = new ArrayList<>();
    }

    protected CompositeMatcher(List<MatcherFunction<?>> matcherFunctions) {
        this();
        this.matcherFunctions.addAll(matcherFunctions);
    }

    protected CompositeMatcher(MatcherFunction<?>... matcherFunctions) {
        this(Arrays.asList(matcherFunctions));
    }

    @Override
    protected boolean matchesSafely(T item) {
        return matcherFunctions.stream().allMatch(
            (MatcherFunction<?> matcherFunction) -> matcherFunction.matches(item));
    }

    @Override
    protected void describeMismatchSafely(T item, Description mismatchDescription) {
        mismatchDescription.appendText("composite(");
        List<SelfDescribing> selfDescribers = new ArrayList<>();
        for (MatcherFunction<?> matcherFunction : matcherFunctions) {
            if (!matcherFunction.matches(item)) {
                Description localDescription = new StringDescription();
                matcherFunction.describeMismatch(item, localDescription);
                selfDescribers.add(new SelfDescribing() {
                    @Override
                    public void describeTo(Description description) {
                        description.appendText(localDescription.toString());
                    }
                });
            }
        }
        mismatchDescription.appendList("", ", ", "", selfDescribers);
        mismatchDescription.appendText(")");
    }

    @Override
    public void describeTo(Description description) {
        description.appendList(
            "composite(", ", ", ")", matcherFunctions);
    }
}
