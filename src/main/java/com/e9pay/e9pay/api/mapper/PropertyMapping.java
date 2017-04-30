package com.e9pay.e9pay.api.mapper;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Represents the mapping of an individual property between a source and target object.
 *
 * @author Vivek Adhikari
 */
@AllArgsConstructor
@EqualsAndHashCode
public class PropertyMapping {

    @Getter
    private String sourceProperty;
    @Getter
    private String targetProperty;
    @Getter
    private boolean readOnly;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
