package ru.job4j.utils;

import org.junit.Test;

import java.text.ParseException;
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
                "23 янв 15, 14:43"
        );
        for (String data : dates) {
            rsl.add(parser.parse(data));
        }
        List<LocalDateTime> expected = List.of(
                LocalDateTime.parse("2021-10-21T10:56"),
                LocalDateTime.parse("2021-10-22T12:11"),
                LocalDateTime.parse("2015-01-23T14:43")
        );
        assertEquals(rsl, expected);
    }
}