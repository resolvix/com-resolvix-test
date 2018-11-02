package com.resolvix.test.beans;

import org.hamcrest.Description;
import org.junit.Test;

import javax.validation.ConstraintViolation;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.groups.Default;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Set;

public class AbstractBeanValidationConstraintViolationMatcherTest {

    static class X {

        public Boolean booleanField;

        @Min(33)
        @Max(35)
        public Short shortField;

        public boolean isTrue() {
            return booleanField.booleanValue();
        }

        public void setTrue(boolean booleanField) {
            this.booleanField = booleanField;
        }
    }

    static class Y extends X {

        public Integer integerField;

        public void setIntegerField(Integer integerField) {
            this.integerField = integerField;
        }

    }

    static class Z extends Y {

        public Long longField;

        public Long getLongField() {
            return longField;
        }

    }

    static class MockConstraintViolationMatcher extends AbstractBeanValidationConstraintViolationMatcher<Z> {

        public PropertyDescriptor[] enumerateProperties(Class<?> beanClass) throws IntrospectionException {
            return super.enumerateProperties(beanClass);
        }

        public PropertyDescriptor[] enumerateProperties(Class<?> beanClass, Class<?> stopClass) throws IntrospectionException {
            return super.enumerateProperties(beanClass, stopClass);
        }

        public Set<ConstraintViolation<Z>> validateProperties(Z z, String[] properties, Class<?>[] groups) {
            return super.validateProperties(z, properties, groups);
        }

        @Override
        public boolean matches(Object o) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void describeTo(Description description) {
            throw new UnsupportedOperationException();
        }
    }

    private static final MockConstraintViolationMatcher MOCK_CONSTRAINT_VIOLATION_MATCHER = new MockConstraintViolationMatcher();

    @Test
    public void enumerateProperties() throws Exception {
        PropertyDescriptor[] propertyDescriptors = MOCK_CONSTRAINT_VIOLATION_MATCHER.enumerateProperties(Z.class);
        for(PropertyDescriptor pd : propertyDescriptors) {
            System.out.println(pd.getName() + " / " + pd.getDisplayName());
            System.out.println();

            Method readMethod = pd.getReadMethod();
            if (readMethod != null)
                System.out.println(pd.getReadMethod().getName());

            Method writeMethod = pd.getWriteMethod();
            if (writeMethod != null)
                System.out.println(pd.getWriteMethod().getName());

            System.out.println();
        }
    }

    @Test
    public void shortField_generates_constraint_violation() throws Exception {
        Z z = new Z();
        z.shortField = 31;

        Set<ConstraintViolation<Z>> constraintViolations = MOCK_CONSTRAINT_VIOLATION_MATCHER
            .validateProperties(z, new String[]{"shortField"}, new Class<?>[]{Default.class});

        constraintViolations.stream().forEach(
            (ConstraintViolation<Z> cv) -> {
                System.out.println(cv.getConstraintDescriptor().getAnnotation().toString());
            }
        );

    }
}
