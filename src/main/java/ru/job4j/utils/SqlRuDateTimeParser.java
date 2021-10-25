package ru.job4j.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {
    private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("dd MM yy");

    private static final Map<String, String> MONTHS = Map.ofEntries(
            Map.entry("янв", "01"),
            Map.entry("фев", "02"),
            Map.entry("мар", "03"),
            Map.entry("апр", "04"),
            Map.entry("май", "05"),
            Map.entry("июн", "06"),
            Map.entry("июл", "07"),
            Map.entry("авг", "08"),
            Map.entry("сен", "09"),
            Map.entry("окт", "10"),
            Map.entry("ноя", "11"),
            Map.entry("дек", "12")
            );

    @Override
    public LocalDateTime parse(String parse) {
        String month = parse.substring(3, 6);
        String[] inputDateTime = parse.split(", ");
        LocalTime time = LocalTime.parse(inputDateTime[1]);
        LocalDate date;
        if (inputDateTime[0].contains("вчера")) {
            date = LocalDate.now().minusDays(1);
        } else if (inputDateTime[0].contains("сегодня")) {
            date = LocalDate.now();
        } else {
            date = LocalDate.parse(inputDateTime[0].replace(month, MONTHS.get(month)), FORMAT);
        }
        return LocalDateTime.of(date, time);
    }
}
