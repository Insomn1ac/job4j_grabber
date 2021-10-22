package ru.job4j.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class SqlRuDateTimeParser implements DateTimeParser {
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
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd MM yy, HH:mm");
        String month = parse.substring(3, 6);
        StringBuilder sb = new StringBuilder();
        if (parse.contains("вчера")) {
            sb.append(parse.replace("вчера, ", LocalDate.now().minusDays(1) + "T"));
            return LocalDateTime.parse(sb);
        } else if (parse.contains("сегодня")) {
            sb.append(parse.replace("сегодня, ", LocalDate.now() + "T"));
            return LocalDateTime.parse(sb);
        } else {
            sb.append(parse.replace(month, MONTHS.get(month)));
            return LocalDateTime.parse(sb.toString(), format);
        }
    }
}
