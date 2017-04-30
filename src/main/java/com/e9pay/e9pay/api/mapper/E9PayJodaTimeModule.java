package com.e9pay.e9pay.api.mapper;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

/**
 * @author Vivek Adhikari
 */
public class E9PayJodaTimeModule extends SimpleModule {

    private static final long serialVersionUID = -6484447538671234147L;

    public E9PayJodaTimeModule() {
        super("pcc-jodatime-module", new Version(1, 0, 0, "SNAPSHOT", "com.e9pay.e9pay", "common"));
        addDeserializer(DateTime.class, new DateTimeDeserializer());
        addDeserializer(LocalDate.class, new DateDeserializer());

        addSerializer(LocalDate.class, new DateSerializer());
        addSerializer(DateTime.class, new DateTimeSerializer());
    }
}
