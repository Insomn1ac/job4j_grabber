package ru.job4j.utils;

import org.junit.Test;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class SqlRuDateTimeParserTest {

    @Test
    public void whenParseAllTypesOfIncomingData() throws ParseException {
        DateTimeParser parser = new SqlRuDateTimeParser();
        List<LocalDateTime> rsl = new ArrayList<>();
        List<String> dates = List.of(
                "вчера, 10:56",
                "сегодня, 12:11",
                "23 янв 15, 14:43",
                "2 дек 19, 22:29"
        );
        for (String data : dates) {
            rsl.add(parser.parse(data));
        }
        List<LocalDateTime> expected = List.of(
                LocalDateTime.parse(LocalDate.now().minusDays(1) + "T10:56"),
                LocalDateTime.parse(LocalDate.now() + "T12:11"),
                LocalDateTime.parse("2015-01-23T14:43"),
                LocalDateTime.parse("2019-12-02T22:29")
        );
        assertEquals(rsl, expected);
    }
}