package com.e9pay.e9pay.api.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.format.Formatter;

/**
 * @author Vivek Adhikari
 * @since 4/21/2017
 */
public class DateFormatter implements Formatter<Date> {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd-yyyy");

    @Override
    public Date parse(String text, Locale locale) throws ParseException {
        //TODO plack logic to convert back date from text
        return new Date();
    }

    @Override
    public String print(Date object, Locale locale) {
        String dateTxt = DATE_FORMAT.format(object);
        return dateTxt;
    }
}
