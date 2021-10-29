package ru.job4j.grabber;

import ru.job4j.Post;
import ru.job4j.utils.SqlRuDateTimeParser;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {

    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("jdbc.url"),
                    cfg.getProperty("jdbc.username"),
                    cfg.getProperty("jdbc.password")
            );
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static Properties getProperties() {
        Properties config = new Properties();
        try (InputStream is = PsqlStore.class.getClassLoader().getResourceAsStream("app.properties")) {
            config.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return config;
    }

    public Post createPost(ResultSet resultSet) {
        Post post = null;
        try {
            post = new Post(resultSet.getInt("id"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getString("link"),
                resultSet.getTimestamp("created").toLocalDateTime());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void save(Post post) {
        try (PreparedStatement statement =
                     cnn.prepareStatement("insert into post(name, description, link, created) values (?, ?, ?, ?)")) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getLink());
            statement.setTimestamp(4, Timestamp.valueOf(post.getCreated()));
            statement.execute();
        } catch (Exception e) {
            System.out.println("Post with non-unique link hasn't been inserted into table");
        }
    }

    @Override
    public List<Post> getAll() {
        var list = new ArrayList<Post>();
        try (PreparedStatement stmt = cnn.prepareStatement("select * from post")) {
            try (ResultSet resultSet = stmt.executeQuery()) {
                while (resultSet.next()) {
                    list.add(createPost(resultSet));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public Post findById(int id) {
        Post post = null;
        try (PreparedStatement stmt = cnn.prepareStatement("select * from post where id = ?")) {
            stmt.setInt(1, id);
            try (ResultSet resultSet = stmt.executeQuery()) {
                if (resultSet.next()) {
                    post = createPost(resultSet);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    public static void main(String[] args) {
            PsqlStore store = new PsqlStore(getProperties());
            SqlRuParse parse = new SqlRuParse(new SqlRuDateTimeParser());
            var posts = parse.list("https://www.sql.ru/forum/job-offers/");
            for (Post post : posts) {
                store.save(post);
            }
            var list = store.getAll();
            for (Post el : list) {
                System.out.println(el);
            }
            System.out.println(store.findById(14));
    }
}
