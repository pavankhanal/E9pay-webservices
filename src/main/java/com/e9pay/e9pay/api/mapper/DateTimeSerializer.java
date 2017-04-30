package com.e9pay.e9pay.api.mapper;

import java.io.IOException;

import com.e9pay.e9pay.api.utils.DateConversionUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

/**
 * @author Vivek Adhikari
 */
public class DateTimeSerializer extends StdSerializer<DateTime> {

    public DateTimeSerializer() {
        super(DateTime.class);
    }

    @Override
    public void serialize(DateTime dateTime, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String dateString = StringUtils.EMPTY;
        if (dateTime != null) {
            dateString = DateConversionUtil.toUserWithTime(dateTime);
        }

        jsonGenerator.writeString(dateString);
    }
}
