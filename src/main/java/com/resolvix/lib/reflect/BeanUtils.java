package com.resolvix.lib.reflect;

import com.resolvix.lib.reflect.api.PropertyDescriptorsNotFoundException;
import com.resolvix.lib.reflect.api.PropertyNotFoundException;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BeanUtils {

    public static final Object[] NO_ARGUMENTS = new Object[] { };

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass)
        throws PropertyDescriptorsNotFoundException {
        if (beanClass == null)
            throw new IllegalArgumentException();

        try {
            return Introspector.getBeanInfo(beanClass)
                .getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new PropertyDescriptorsNotFoundException(e);
        }
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass, Class<?> stopClass)
        throws PropertyDescriptorsNotFoundException {
        if (beanClass == null)
            throw new IllegalArgumentException();
        if (stopClass == null)
            throw new IllegalArgumentException();

        try {
            return Introspector.getBeanInfo(beanClass, stopClass)
                .getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new PropertyDescriptorsNotFoundException(e);
        }
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Object bean)
        throws PropertyDescriptorsNotFoundException {
        if (bean == null)
            throw new IllegalArgumentException();
        return getPropertyDescriptors(bean.getClass());
    }

    public static PropertyDescriptor[] getPropertyDescriptors(Object bean, Class<?> stopClass)
        throws PropertyDescriptorsNotFoundException {
        if (bean == null)
            throw new IllegalArgumentException();
        if (stopClass == null)
            throw new IllegalArgumentException();
        return getPropertyDescriptors(bean.getClass(), stopClass);
    }

    public static PropertyDescriptor getPropertyDescriptor(Class<?> beanClass, String propertyName)
        throws PropertyNotFoundException {
        if (beanClass == null)
            throw new IllegalArgumentException();
        if (propertyName == null || propertyName.isEmpty())
            throw new IllegalArgumentException();

        try {
            PropertyDescriptor[] propertyDescriptors = getPropertyDescriptors(beanClass);
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
                if (propertyDescriptor.getName().equals(propertyName))
                    return propertyDescriptor;
            throw new PropertyNotFoundException();
        } catch (PropertyDescriptorsNotFoundException e) {
            throw new com.resolvix.lib.reflect.api.PropertyNotFoundException();
        }
    }

    public static PropertyDescriptor getPropertyDescriptor(Object bean, String propertyName)
        throws PropertyNotFoundException {
        if (bean == null)
            throw new IllegalArgumentException();
        return getPropertyDescriptor(bean.getClass(), propertyName);
    }

    private Object getProperty(Object object, String propertyName)
        throws PropertyNotFoundException, IllegalAccessException, InvocationTargetException {
        if (object == null)
            throw new IllegalArgumentException();
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(object.getClass(), propertyName);
        Method readMethod = propertyDescriptor.getReadMethod();
        return readMethod.invoke(object, NO_ARGUMENTS);
    }

    public <V> void setProperty(Object object, String propertyName, V v)
        throws PropertyNotFoundException, IllegalAccessException, InvocationTargetException {
        if (object == null)
            throw new IllegalArgumentException();
        PropertyDescriptor propertyDescriptor = getPropertyDescriptor(object.getClass(), propertyName);
        Method writeMethod = propertyDescriptor.getWriteMethod();
        writeMethod.invoke(object, v);
    }
}
