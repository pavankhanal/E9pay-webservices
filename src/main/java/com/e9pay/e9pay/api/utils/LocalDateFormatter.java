package com.e9pay.e9pay.api.utils;

import java.text.ParseException;
import java.util.Locale;

import org.springframework.format.Formatter;

import org.joda.time.LocalDate;

/**
 * @author Jonathan Cone
 */
public class LocalDateFormatter implements Formatter<LocalDate> {

    @Override
    public LocalDate parse(String text, Locale locale) throws ParseException {
        return DateConversionUtil.toSystemWithoutTime(text);
    }

    @Override
    public String print(LocalDate object, Locale locale) {
        return DateConversionUtil.toUserWithoutTime(object);
    }
}
