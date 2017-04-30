package com.e9pay.e9pay.api.mapper;

import java.io.IOException;

import com.e9pay.e9pay.api.utils.DateConversionUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

/**
 * @author  Vivek Adhikari
 */

public class DateDeserializer extends StdDeserializer<LocalDate> {

    private static final long serialVersionUID = -7123083705897710329L;

    public DateDeserializer() {
        super(LocalDate.class);
    }

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final String dateValue = jsonParser.getText().trim();
        if (StringUtils.isNotEmpty(dateValue)) {
            return DateConversionUtil.toSystemWithoutTime(dateValue);
        }
        return null;
    }
}
