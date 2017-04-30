package com.e9pay.e9pay.api.mapper;

import java.io.IOException;

import com.e9pay.e9pay.api.utils.DateConversionUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalDate;

/**
 * @author Vivek Adhikari
 */
public class DateSerializer extends StdSerializer<LocalDate> {

    public DateSerializer() {
        super(LocalDate.class);
    }

    @Override
    public void serialize(LocalDate date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        String dateString = StringUtils.EMPTY;
        if (date != null) {
            dateString = DateConversionUtil.toUserWithoutTime(date);
        }

        jsonGenerator.writeString(dateString);

    }
}
