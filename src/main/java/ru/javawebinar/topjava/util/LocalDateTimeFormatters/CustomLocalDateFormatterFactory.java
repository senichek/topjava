package ru.javawebinar.topjava.util.LocalDateTimeFormatters;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class CustomLocalDateFormatterFactory implements AnnotationFormatterFactory<LocalDateFormat> {
    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(LocalDateTime.class));
    }

    @Override
    public Printer<LocalDate> getPrinter(LocalDateFormat annotation, Class<?> fieldType) {
        return new CustomLocalDateFormatter();
    }

    @Override
    public Parser<LocalDate> getParser(LocalDateFormat annotation, Class<?> fieldType) {
        return new CustomLocalDateFormatter();
    }
}
