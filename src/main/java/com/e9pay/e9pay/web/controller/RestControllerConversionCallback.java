package com.e9pay.e9pay.web.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.convert.ConversionException;
import org.springframework.core.convert.ConversionService;

import lombok.Getter;

import com.e9pay.e9pay.api.mapper.ConversionCallback;
import com.e9pay.e9pay.api.utils.BeanPropertyUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Simple {@link ConversionCallback} that can make use of Spring's {@link ConversionService} to convert properties of a JavaBean from Strings to
 * their
 * canonical Java type and perform validation when a failure occurs.
 *
 * @author Vivek Adhikari
 */
public class RestControllerConversionCallback implements ConversionCallback<Object> {

    private static final String COMMA = ",";

    private final ConversionService conversionService;
    private final Class<?> targetClass;

    @Getter
    private final List<RestValidationIssue> issues;

    public RestControllerConversionCallback(
        ConversionService conversionService,
        Class<?> targetClass
    ) {
        this.conversionService = conversionService;
        this.targetClass = targetClass;
        issues = new ArrayList<>();
    }

    public boolean hasIssues() {
        return CollectionUtils.isNotEmpty(issues);
    }

    @Override
    public Object convert(String property, Object value) {

        final Class<?> targetType = BeanPropertyUtils.getNestedPropertyType(targetClass, property);

        Object converted = null;

        if (targetType != null) {

            try {
                if (value instanceof String[]) {
                    converted = conversionService.convert(
                        StringUtils.join((Object[]) value, COMMA),
                        (Class<?>) Array.newInstance(targetType, 0).getClass()
                    );
                } else {
                    converted = conversionService.convert(value, targetType);
                }
            } catch (ConversionException ignore) {
                issues.add(new RestValidationIssue(
                    property,
                    String.format("The filter value '%s' needs to be convertable to the type '%s'", value, targetType.getSimpleName())
                ));
            }
        }

        return converted;
    }
}
