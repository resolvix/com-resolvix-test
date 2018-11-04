package com.resolvix.test.beans;

import com.resolvix.lib.reflect.BeanUtils;
import com.resolvix.lib.reflect.api.PropertyNotFoundException;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.hamcrest.beans.PropertyUtil;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

public class BeanPropertyMatcher<T> extends TypeSafeDiagnosingMatcher<T> {

    private static final String PROPERTY_SEPARATOR = ".";

    private static final String PROPERTY_DESCRIPTION_SEPARATOR = " ";

    private static final String PROPERTY_DESCRIPTION_COMMA_SEPARATOR = ", ";

    private static final String NO_PROPERTY_DESCRIPTION_TEMPLATE = "No property \"%s\"";

    private static final String NON_READABLE_PROPERTY_DESCRIPTION_TEMPLATE = "Property \"%s\" is not readable.";

    private static final String PROPERTY_DESCRIPTION_TEMPLATE = "property %s = ";

    private static final String PROPERTY_VALUE_TEMPLATE = "property \"%s\"";

    private String propertyName;
    private Matcher<?> valueMatcher;

    private BeanPropertyMatcher(String propertyName, Matcher<?> valueMatcher) {
        this.propertyName = propertyName;
        this.valueMatcher = valueMatcher;
    }

    public static <T> BeanPropertyMatcher<T> of(String propertyName, Matcher<?> valueMatcher) {
        return new BeanPropertyMatcher(propertyName, valueMatcher);
    }

    private String getMemberObjectProperty(String propertyName) {
        return propertyName.substring(0, propertyName.indexOf(PROPERTY_SEPARATOR));
    }

    private Object getPropertyValue(Object object, String propertyName)
        throws Exception {
        PropertyDescriptor propertyDescriptor = PropertyUtil.getPropertyDescriptor(propertyName, object);
        Method readMethod = propertyDescriptor.getReadMethod();
        return readMethod.invoke(object, PropertyUtil.NO_ARGUMENTS);
    }

    private String getSubMemberObjectProperty(String propertyName) {
        return propertyName.substring(propertyName.indexOf(PROPERTY_SEPARATOR) + 1);
    }

    private Method getReadMethodForProperty(Object object, String propertyName, Description mismatchDescription) {
        try {
            Method readMethod = BeanUtils.getPropertyDescriptor(object, propertyName)
                .getReadMethod();
            if (readMethod == null)
                mismatchDescription.appendText(
                    String.format(NON_READABLE_PROPERTY_DESCRIPTION_TEMPLATE, propertyName));
            return readMethod;
        } catch (PropertyNotFoundException e) {
            mismatchDescription.appendText(
                String.format(NO_PROPERTY_DESCRIPTION_TEMPLATE, propertyName));
            return null;
        }
    }

    private boolean matchPropertyValue(Object bean, Method readMethod, Description mismatchDescription)
            throws Exception {
        Object propertyValue = readMethod.invoke(bean, PropertyUtil.NO_ARGUMENTS);
        boolean valueMatches = valueMatcher.matches(propertyValue);
        if (!valueMatches) {
            if (!mismatchDescription.toString().isEmpty())
                mismatchDescription.appendText(", ");
            mismatchDescription.appendText(
                String.format(PROPERTY_VALUE_TEMPLATE, propertyName));
            valueMatcher.describeMismatch(propertyValue, mismatchDescription);
        }
        return valueMatches;
    }

    private boolean matchesSafely(Object bean, String propertyName, Description mismatchDescription)
        throws Exception {
        Object parentObject = bean;
        if (propertyName.contains(PROPERTY_SEPARATOR)) {
            String memberObjectProperty = getMemberObjectProperty(propertyName);
            Object memberObject = getPropertyValue(parentObject, memberObjectProperty);
            String subMemberObjectProperty = getSubMemberObjectProperty(propertyName);
            return matchesSafely(memberObject, subMemberObjectProperty, mismatchDescription);
        } else {
            Method readMethod = getReadMethodForProperty(bean, propertyName, mismatchDescription);
            return (readMethod != null)
                && matchPropertyValue(bean, readMethod, mismatchDescription);
        }
    }

    @Override
    public boolean matchesSafely(T beanT, Description mismatchDescription) {
        try {
            return matchesSafely(beanT, propertyName, mismatchDescription);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void describeTo(Description description) {
        if (description.toString().length() > 0)
            description.appendText(PROPERTY_DESCRIPTION_COMMA_SEPARATOR);
        description.appendText(
            String.format(PROPERTY_DESCRIPTION_TEMPLATE, propertyName));
        description.appendDescriptionOf(valueMatcher);
        description.appendText(PROPERTY_DESCRIPTION_SEPARATOR);
    }
}
