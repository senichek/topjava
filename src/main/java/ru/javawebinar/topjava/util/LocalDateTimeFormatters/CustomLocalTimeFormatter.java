package ru.javawebinar.topjava.util.LocalDateTimeFormatters;

import org.springframework.format.Formatter;
import ru.javawebinar.topjava.util.DateTimeUtil;

import java.text.ParseException;
import java.time.LocalTime;
import java.util.Locale;

public class CustomLocalTimeFormatter implements Formatter<LocalTime> {
    @Override
    public LocalTime parse(String text, Locale locale) throws ParseException {
        return DateTimeUtil.parseLocalTime(text);
    }

    @Override
    public String print(LocalTime object, Locale locale) {
        return object.toString();
    }
}
