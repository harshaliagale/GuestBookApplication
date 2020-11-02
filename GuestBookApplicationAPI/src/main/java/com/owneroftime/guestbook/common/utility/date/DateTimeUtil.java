package com.owneroftime.guestbook.common.utility.date;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Locale;

public class DateTimeUtil {
    private DateTimeUtil(){}

    public static String convertLocalDateTimeToString(LocalDateTime localDateTime) {
        DateTimeFormatter df = new DateTimeFormatterBuilder()
                // case insensitive to parse JAN and FEB
                .parseCaseInsensitive()
                // add pattern
                .appendPattern("yyyy-MM-dd HH:mm")
                // create formatter (use English Locale to parse month names)
                .toFormatter(Locale.ENGLISH);
        return localDateTime.format(df);
    }

    
}
