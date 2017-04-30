package com.e9pay.e9pay.api.mapper;

import java.io.IOException;

import com.e9pay.e9pay.api.utils.DateConversionUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * @author Vivek Adhikari
 */
public class DateTimeDeserializer extends StdDeserializer<DateTime> {

    private static final long serialVersionUID = 2457543550391621538L;

    @SuppressWarnings("WeakerAccess")
    public DateTimeDeserializer() {
        super(DateTime.class);
    }

    @Override
    public DateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        final String dateValue = jsonParser.getText().trim();

        if (StringUtils.isEmpty(dateValue)) {
            return null;
        }

        return DateConversionUtil.toSystemWithTime(dateValue);
    }
}
