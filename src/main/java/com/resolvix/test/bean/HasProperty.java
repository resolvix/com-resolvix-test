package com.resolvix.test.bean;

import com.resolvix.lib.reflect.BeanUtils;
import com.resolvix.lib.reflect.api.Path;
import com.resolvix.lib.reflect.api.PropertyNotFoundException;
import com.resolvix.lib.reflect.api.PropertyNotReadableException;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;

public class HasProperty<T> extends TypeSafeDiagnosingMatcher<T> {

    private static final int DEFAULT_PROPERTY_SEPARATOR = '.';

    private static final String PROPERTY_DESCRIPTION_SEPARATOR = " ";

    private static final String PROPERTY_DESCRIPTION_COMMA_SEPARATOR = ", ";

    private static final String ACCESS_DENIED_PROPERTY_DESCRIPTION_TEMPLATE = "Property \"%s\" cannot be accessed";

    private static final String NO_PROPERTY_DESCRIPTION_TEMPLATE = "no property \"%s\" is available";

    private static final String NON_READABLE_PROPERTY_DESCRIPTION_TEMPLATE = "Property \"%s\" is not readable";

    private static final String INVOCATION_EXCEPTION_PROPERTY_DESCRIPTION_TEMPLATE = "The getter for property \"%s\" generated an exception";

    private static final String PROPERTY_DESCRIPTION_TEMPLATE = "property \"%s\" is ";

    private static final String PROPERTY_VALUE_TEMPLATE = "property \"%s\" ";

    private static final String NULL_PATH = "(null)";

    private Path propertyPath;

    private Matcher<?> valueMatcher;


    private HasProperty(String propertyPath, Matcher<?> valueMatcher) {
        this.propertyPath = BeanUtils.toPath(propertyPath, DEFAULT_PROPERTY_SEPARATOR);
        this.valueMatcher = valueMatcher;
    }

    public static <T> HasProperty<T> of(String propertyPath, Matcher<?> valueMatcher) {
        return new HasProperty<>(propertyPath, valueMatcher);
    }

    private String safePath(Path.Node node) {
        if (node == null)
            return NULL_PATH;
        return node.getPath();
    }

    @Override
    public boolean matchesSafely(Object bean, Description mismatchDescription) {
        Object object = bean;
        Iterator<Path.Node> it = propertyPath.iterator();
        Path.Node node = null;
        try {
            if (!it.hasNext())
                throw new IllegalStateException();

            do {
                node = it.next();
                if (node == null)
                    throw new IllegalStateException();

                object = BeanUtils.getProperty(object, node.getName());
            } while (it.hasNext());

            boolean valueMatches = valueMatcher.matches(object);
            if (!valueMatches) {
                mismatchDescription.appendText(
                    String.format(PROPERTY_VALUE_TEMPLATE, propertyPath.getPath()));
                valueMatcher.describeMismatch(object, mismatchDescription);
            }

            return valueMatches;
        } catch (PropertyNotFoundException e) {
            mismatchDescription.appendText(
                String.format(NO_PROPERTY_DESCRIPTION_TEMPLATE, safePath(node)));
        } catch (PropertyNotReadableException e) {
            mismatchDescription.appendText(
                String.format(NON_READABLE_PROPERTY_DESCRIPTION_TEMPLATE, safePath(node)));
        } catch (InvocationTargetException e) {
            mismatchDescription.appendText(
                String.format(INVOCATION_EXCEPTION_PROPERTY_DESCRIPTION_TEMPLATE, safePath(node)));
        } catch (IllegalAccessException e) {
            //
            //  The `IllegalAccessException` is effectively masked by the
            //  `PropertyNotFoundException` and should not, therefore, occur
            //  under any circumstances.
            //
            mismatchDescription.appendText(
                String.format(ACCESS_DENIED_PROPERTY_DESCRIPTION_TEMPLATE, safePath(node)));
        }

        return false;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(
            String.format(PROPERTY_DESCRIPTION_TEMPLATE, propertyPath.getPath()));
        description.appendDescriptionOf(valueMatcher);
    }
}
