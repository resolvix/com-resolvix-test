package com.resolvix.test.entity;

import com.resolvix.lib.reflect.BeanUtils;
import com.resolvix.lib.reflect.api.PropertyNotFoundException;
import com.resolvix.lib.reflect.api.PropertyNotReadableException;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.function.Supplier;

@Deprecated
public class EntityMatcher<T> extends TypeSafeDiagnosingMatcher<Supplier<T>> {

    private static final String TYPE_CANNOT_BE_NULL = "type cannot be null";

    private static final String VALUE_CANNOT_BE_NULL_FOR_A_PRIMITIVE = "value cannot be null for a primitive";

    private static final String PROPERTY_EXPECTATIONS_TEMPLATE = "property %s should contain %s";

    private static final String PROPERTY_TYPE_EXPECTATIONS_TEMPLATE = "property %s should be of type %s and value %s";

    private static final String PROPERTY_MISMATCH_TEMPLATE = "property %s is %s";

    private static final String PROPERTY_TYPE_MISMATCH_TEMPLATE = "property %s is of type %s and value %s";

    private static final String PROPERTY_NOT_FOUND_TEMPLATE = "property %s is not found";

    private static final String PROPERTY_NOT_READABLE_TEMPLATE = "property %s is not readable";

    private static final String PROPERTY_NOT_ACCESSIBLE_TEMPLATE = "property %s is not accessible";

    private static final String PROPERTY_ACCESS_TRIGGERED_EXCEPTION_TEMPLATE = "property triggered exception %s";

    public static class PropertyValue {

        private String name;

        private Class<?> type;

        private Object value;

        public PropertyValue(
            String name,
            Class<?> type,
            Object value
        ) {
            if (type == null)
                throw new IllegalArgumentException(TYPE_CANNOT_BE_NULL);

            if (type.isPrimitive() && value == null)
                throw new IllegalArgumentException(VALUE_CANNOT_BE_NULL_FOR_A_PRIMITIVE);

            this.name = name;
            this.type = type;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public Class<?> getType() {
            return type;
        }

        public Object getValue() {
            return value;
        }
    }


    private PropertyValue[] propertyValues;

    public EntityMatcher(
        PropertyValue... propertyValues
    ) {
        this.propertyValues = propertyValues;
    }

    private String safeValue(Object object) {
        if (object == null)
            return "null";
        return object.toString();
    }

    private boolean typeEquals(Class<?> classL, Class<?> classR) {
        if (classR.isPrimitive()) {
            try {
                Field typeField = classL.getField("TYPE");
                Class<?> primitiveTypeClassL = (Class<?>) typeField.get(null);
                if (primitiveTypeClassL == null)
                    return false;

                return primitiveTypeClassL.equals(classR);
            } catch (NoSuchFieldException e) {
                return false;
            } catch (IllegalAccessException e) {
                return false;
            }
        }

        return classL.equals(classR);
    }

    private boolean matchesSafely(T t, PropertyValue propertyValue, Description description) {
        String name = propertyValue.getName();
        Class<?> type = propertyValue.getType();
        Object value = propertyValue.getValue();
        try {
            Object object = BeanUtils.getProperty(t, name);
            if (object == null && type.isPrimitive()) {
                description.appendText(
                    String.format("a property that is expected to return a primitive cannot have a null value"));
                return false;
            }

            if (object == null && value == null)
                // a null value can be acceptable for a non-primitive
                // object.
                return true;

            if (object == null && value != null
                || object != null && value == null
                || value != null && object != null
                && !value.equals(object)
                && type.equals(value.getClass())) {
                description.appendText(
                    String.format(PROPERTY_EXPECTATIONS_TEMPLATE, name, safeValue(value)));
                description.appendText(
                    String.format(PROPERTY_MISMATCH_TEMPLATE, name, safeValue(object)));
                return false;
            } else if (!typeEquals(value.getClass(), type)) {
                description.appendText(
                    String.format(PROPERTY_TYPE_EXPECTATIONS_TEMPLATE, name, type, value));
                description.appendText(
                    String.format(PROPERTY_TYPE_MISMATCH_TEMPLATE, name, object.getClass().getName(), object.toString()));
                return false;
            }

            return true;
        } catch (PropertyNotFoundException e) {
            description.appendText(
                String.format(PROPERTY_NOT_FOUND_TEMPLATE, name));
        } catch (PropertyNotReadableException e) {
            description.appendText(
                String.format(PROPERTY_NOT_READABLE_TEMPLATE, name));
        } catch (IllegalAccessException e) {
            description.appendText(
                String.format(PROPERTY_NOT_ACCESSIBLE_TEMPLATE, name));
        } catch (InvocationTargetException e) {
            description.appendText(
                String.format(PROPERTY_ACCESS_TRIGGERED_EXCEPTION_TEMPLATE, name));
        }

        return false;
    }

    @Override
    protected boolean matchesSafely(Supplier<T> supplierT, Description description) {
        T t = supplierT.get();
        return Arrays.stream(propertyValues)
            .allMatch((PropertyValue propertyValue) -> {
                return matchesSafely(t, propertyValue, description);
            });
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("EntityMatcher");
    }
}
