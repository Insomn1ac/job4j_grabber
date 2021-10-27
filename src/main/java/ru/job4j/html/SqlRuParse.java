package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.IOException;

public class SqlRuParse {

    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= 5; i++) {
            Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers/" + i).get();
            Elements row = doc.select(".postslisttopic");
            for (Element td : row) {
                Element href = td.child(0);
                Element parent = td.parent();
                System.out.println(href.attr("href"));
                System.out.println(href.text());
                System.out.println(parent.children().get(5).text());
                System.out.println("---------------");
            }
        }
        String rsl = getParse("https://www.sql.ru/forum/1325330/lidy-be-fe-senior-cistemnye-analitiki-qa-i-devops-moskva-do-200t");
        System.out.println(rsl);
    }

    public static String getParse(String url) throws IOException {
        SqlRuDateTimeParser parser = new SqlRuDateTimeParser();
        Document doc = Jsoup.connect(url).get();
        Element description = doc.select(".msgBody").get(1);
        Element time = doc.select(".msgFooter").get(0);
        String str = time.text().split(" \\[")[0];
        return parser.parse(str) + System.lineSeparator() + description.text();
    }
}
