package com.e9pay.e9pay.api.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class for date conversion
 *
 * @author Vivek Adhikari
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@SuppressWarnings("PMD.UseUtilityClass")
public class DateConversionUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateConversionUtil.class);

    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern(DATE_TIME_FORMAT);

    public static final DateTimeZone UTC_TIME_ZONE = DateTimeZone.forID("Etc/UTC");

    public static final DateTimeZone USER_TIME_ZONE = DateTimeZone.forID("Etc/GMT+9");

    /**
     * Improtant
     * User based timzone is not implemented now. However application architechture support this.
     * Now we have constant as User time zone, later  this should replaced with current user timezone
     * when need to facilate based on timezone.
     */
    private static DateTimeZone getCurrentUserTimeZone() {
        return USER_TIME_ZONE;
    }

    /**
     * Get the current {@link DateTime} using the system time zone.  The system time zone must be in UTC.
     *
     * @return The current {@code DateTime} in UTC
     */
    public static DateTime getCurrentDateTimeSystem() {
        return new DateTime();
    }

    public static String toUserWithTime(DateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return new DateTime(dateTime)
            .toDateTime(getCurrentUserTimeZone())
            .toString(DATE_TIME_FORMAT);
    }

    public static String toUserWithTime(DateTime dateTime, String format, DateTimeZone dateTimeZone) {
        if (dateTime == null) {
            return null;
        }

        return new DateTime(dateTime)
            .toDateTime(dateTimeZone)
            .toString(format);
    }

    /**
     * Convert a {@link LocalDate} to a date string.  The string is formatted using {@link #DATE_FORMAT}.
     *
     * @param localDate
     *     {@code LocalDate} to convert to a string.
     *
     * @return A string representation of {@code localDate}.  If {@code localDate} is null, null will be returned.
     */
    public static String toUserWithoutTime(LocalDate localDate) {
        return toUserWithoutTime(localDate, DATE_FORMAT);
    }

    /**
     * Convert a {@link LocalDate} to a date string.  The string is formatted using the format provided.
     *
     * @param localDate
     *     {@code LocalDate} to convert to a string.
     * @param format
     *     Format of string to return.  See {@link DateTimeFormat}.
     *
     * @return A string representation of {@code localDate}.  If {@code localDate} is null, null will be returned.
     */
    public static String toUserWithoutTime(LocalDate localDate, String format) {
        if (localDate == null) {
            return null;
        }

        return localDate.toString(format);
    }

    public static DateTime toSystemWithTime(String userTime) {
        if (userTime == null) {
            return null;
        }

        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(DATE_TIME_FORMAT).withZone(getCurrentUserTimeZone());

        return dateTimeFormatter.parseDateTime(userTime).toDateTime().withZone(UTC_TIME_ZONE);
    }

    static DateTime toSystemWithTime(String userTime, String format, DateTimeZone dateTimeZone) {
        if (userTime == null) {
            return null;
        }

        final DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format).withZone(dateTimeZone);

        return dateTimeFormatter.parseDateTime(userTime).toDateTime().withZone(UTC_TIME_ZONE);
    }

    /**
     * Convert a {@link String} date to a {@link LocalDate}.  The string must be in the format {@link #DATE_FORMAT}.
     *
     * @param userTime
     *     A string in the format {@link #DATE_FORMAT}
     *
     * @return A {@code LocalDate} object.  If {@code userTime} is null, null will be returned.
     */
    public static LocalDate toSystemWithoutTime(String userTime) {
        return toSystemWithoutTime(userTime, DATE_FORMAT);
    }

    /**
     * Convert a {@link String} date to a {@link LocalDate}.  The string must be in the format provided.
     *
     * @param userTime
     *     A string in the format provided
     *
     * @return A {@code LocalDate} object.  If {@code userTime} is null, null will be returned.
     */
    public static LocalDate toSystemWithoutTime(String userTime, String format) {
        if (userTime == null) {
            return null;
        }

        final DateTimeFormatter dateFormatter = DateTimeFormat.forPattern(format);

        return dateFormatter.parseLocalDate(userTime);
    }

    /**
     * Convert a {@link DateTime} to a {@link LocalDate} by first converting the dateTime to the timeZone passed in.
     *
     * @param timeZone
     *     The time zone to use for the convert.
     * @param dateTime
     *     {@link DateTime} to convert.
     *
     * @return {@link LocalDate} for timeZone
     */
    public static LocalDate toLocalDateWithZoneFromUtc(DateTimeZone timeZone, DateTime dateTime) {

        return dateTime.withZone(timeZone).toLocalDate();
    }
}
