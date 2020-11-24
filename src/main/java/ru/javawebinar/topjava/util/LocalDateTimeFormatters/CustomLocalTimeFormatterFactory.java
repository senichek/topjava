package ru.javawebinar.topjava.util.LocalDateTimeFormatters;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CustomLocalTimeFormatterFactory implements AnnotationFormatterFactory<LocalTimeFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(LocalDateTime.class));
    }

    @Override
    public Printer<LocalTime> getPrinter(LocalTimeFormat annotation, Class<?> fieldType) {
        return new CustomLocalTimeFormatter();
    }

    @Override
    public Parser<LocalTime> getParser(LocalTimeFormat annotation, Class<?> fieldType) {
        return new CustomLocalTimeFormatter();
    }
}
