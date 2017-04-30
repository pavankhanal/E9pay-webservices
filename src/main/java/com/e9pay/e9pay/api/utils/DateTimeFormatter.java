package com.e9pay.e9pay.api.utils;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * @author Vivek Adhikari
 * @since 4/21/2017
 */
public class DateTimeFormatter implements Formatter<DateTime> {

    private DateTimeZone dateTimeZone;

    public DateTimeFormatter() {
        this(DateConversionUtil.UTC_TIME_ZONE);
    }

    DateTimeFormatter(DateTimeZone dateTimeZone) {
        this.dateTimeZone = dateTimeZone;
    }

    @Override
    public DateTime parse(String text, Locale locale) throws ParseException {
        return DateConversionUtil.toSystemWithTime(text, DateConversionUtil.DATE_TIME_FORMAT, dateTimeZone);
    }

    @Override
    public String print(DateTime object, Locale locale) {
        return DateConversionUtil.toUserWithTime(object, DateConversionUtil.DATE_TIME_FORMAT, dateTimeZone);
    }
}
