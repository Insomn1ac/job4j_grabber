package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.Post;
import ru.job4j.utils.DateTimeParser;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SqlRuParse implements Parse {
    private final DateTimeParser dateTimeParser;

    public SqlRuParse(DateTimeParser dateTimeParser) {
        this.dateTimeParser = dateTimeParser;
    }

    @Override
    public List<Post> list(String link) {
        List<Post> list = new ArrayList<>();
        int id = 0;
        try {
            for (int i = 1; i <= 5; i++) {
                Document doc = Jsoup.connect(link + i).get();
                Elements row = doc.select(".postslisttopic");
                for (Element td : row) {
                    Element href = td.child(0);
                    String url = href.attr("href");
                    String title = href.text();
                    Post post = detail(url);
                    post.setId(id++);
                    post.setLink(url);
                    post.setTitle(title);
                    list.add(post);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Post detail(String link) {
        Post post = new Post();
        try {
            Document vacancy = Jsoup.connect(link).get();
            String description = vacancy.select(".msgBody").get(1).text();
            String time = vacancy.select(".msgFooter").get(0).text();
            LocalDateTime timeOfPost = dateTimeParser.parse(time.split(" \\[")[0]);
            post.setDescription(description);
            post.setCreated(timeOfPost);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return post;
    }

    public static void main(String[] args) {
        SqlRuParse parse = new SqlRuParse(new SqlRuDateTimeParser());
        System.out.println(parse.list("https://www.sql.ru/forum/job-offers/"));
    }
}
