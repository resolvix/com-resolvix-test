# Resolvix Java Test Library 

A library to support test driven development ("**TDD**") practices with the
object of reducing software development overhead.

This library is a work-in-progress and is subject to periodic change and 
re-organization.

## Entity Classes 

Hamcrest -derived matchers to support the analysis of classes that implement
"_entity classes_", plain-old-Java-objects that are intended to contain data
from a variety of sources, and allow that data to be retrieved, using `get` 
\[and `is` (?)\] methods, in their original or a near-original form.

Entity classes subject to analysis may be created and initialised as follows -

1.  with data from a `java.sql.ResultSet`, where the entity class is created
and initialised using a factory method; and
2.  with data initialised using an approach comparable to that used for the
Java Persistence API ("**JPA**").

### EntityMatcher

Hamcrest -derived matcher to identify one or more mismatches in the
initialisation of an instance of an _entity class_ by a supplier method:
a constructor, or a factory method.

#### Usage

    assertThat(
        <Supplier<entityClass>>,
        new EntityMatcher(
            PropertyValue(<name>, <type>, <value>),
            ...));

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
                <valueMatcher>),
            BeanMatchers.property(
                <propertyPath>,
                <valueMatcher>));

### BeanPropertyMatcher

Hamcrest -derived matcher to identify mismatches in bean property values.

#### Usage
    
    assertThat(
        <bean>,
        BeanMatchers.property(
        <propertyPath>,
        <valueMatcher>));

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

