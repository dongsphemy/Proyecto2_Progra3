package org.example.common.wrappers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class localDateAdapter {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

       public static LocalDate fromString(String s) {
        return s == null ? null : LocalDate.parse(s, FORMATTER);
    }

    public static String toString(LocalDate date) {
        return date == null ? null : date.format(FORMATTER);
    }

}
