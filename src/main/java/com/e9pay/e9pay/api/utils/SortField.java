package com.e9pay.e9pay.api.utils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Vivek Adhikari
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SortField {
    int order() default 0; /* 0 based */
    SortDirection direction() default SortDirection.ASC;
}
