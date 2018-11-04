# Resolvix Test Library 

A library to support test driven development practices.

This library is a work-in-progress and is subject to periodic change and re-organization.

## Java Beans

Hamcrest -derived matchers to support the analysis of Java Beans either as
input to a method under test, or as output from a method under test.

### BeanMatcher

Hamcrest -derived matcher to identify one or more mismatches in bean
property values.

#### Usage

    assertThat(
        <bean>,
        BeanMatchers.has(
            BeanMatchers.property(
                <propertyPath>,
                <Matcher<?>>),
            BeanMatchers.propery(
                <propertyPath>,
                <Matcher<?>>));

### BeanPropertyMatcher

Hamcrest -derived matcher to identify mismatches in bean property values.

#### Usage
    
    assertThat(
        <bean>,
        BeanMatchers.property(
        <propertyPath>,
        <Matcher<?>>));

### GeneratesConstraintViolations

Hamcrest -derived matcher to identify JSR-303 Bean Validation constraint
violations for a subject bean.

#### Usage

    <not implemented>


### GeneratesNoConstraintViolations

Hamcrest -derived matcher to ensure that a subject bean does not violate any
JSR-303 Bean Validation constraints.

#### Usage

    <not implemented>

