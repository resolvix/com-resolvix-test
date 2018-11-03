package com.resolvix.test.beans;

import javax.validation.*;

import org.hamcrest.beans.PropertyUtil;
import org.hibernate.validator.HibernateValidator;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Member;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

abstract class AbstractBeanValidationConstraintViolationMatcher<T>
    extends BaseMatcher<T>
{
    protected AbstractBeanValidationConstraintViolationMatcher() { }

    protected PropertyDescriptor[] enumerateProperties(Class<?> beanClass) throws IntrospectionException {
        return Introspector.getBeanInfo(beanClass)
            .getPropertyDescriptors();
    }

    protected PropertyDescriptor[] enumerateProperties(Class<?> beanClass, Class<?> stopClass) throws IntrospectionException {
        return Introspector.getBeanInfo(beanClass, stopClass)
            .getPropertyDescriptors();
    }

    private Member[] getMembers(PropertyDescriptor[] propertyDescriptors) {
        Set<Member> members = new HashSet<Member>();
        Arrays.stream(propertyDescriptors).forEach(
            (PropertyDescriptor pd) -> {
                Member readMethod = pd.getReadMethod();
                if (readMethod != null)
                    members.add(readMethod);

                Member writeMethod = pd.getWriteMethod();
                if (writeMethod != null)
                    members.add(writeMethod);
            }
        );
        return members.toArray(new Member[]{ });
    }

    protected Member[] enumerateMembers(Class<?> beanClass) throws IntrospectionException {
        PropertyDescriptor[] propertyDescriptors = enumerateProperties(beanClass);
        return new Member[]{};
    }

    protected Set<ConstraintViolation<T>> validateProperties(T t, String[] properties, Class<?>[] groups) {
        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory(); //new LocalValidatorFactoryBean();

        try {
            Validator validator = validatorFactory.getValidator();
            Set<ConstraintViolation<T>> constraintViolations = new HashSet<ConstraintViolation<T>>();
            Arrays.stream(properties).forEach(
                (String property) -> {
                    constraintViolations.addAll(
                    validator.validateProperty(t, property, groups));
                }
            );
            return constraintViolations;
        } finally {
            //validatorFactory.close();
        }
    }
}
