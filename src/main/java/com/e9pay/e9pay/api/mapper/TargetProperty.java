package com.e9pay.e9pay.api.mapper;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation signfies that a property mapping should be overridden to point to a different property on the target object.  By default, property
 * mappings will be associated to the target field with the same name and path.
 *
 * @author Vivek Adhikari
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
@Documented
@Inherited
public @interface TargetProperty {

    /**
     * @return The property name on the target object that this annotated property should map to instead.  Can contain nested paths, i.e.
     * "person.home.street" to refer to child properties.  Defaults to the name of the property that is annotated.
     */
    String value() default "";

    /**
     * @return true if this property should be read-only on the target object, defaults to false.
     */
    boolean readOnly() default false;

    /**
     * @return true if this property should be ignored during any attempt to process it.
     */
    boolean ignore() default false;
}
