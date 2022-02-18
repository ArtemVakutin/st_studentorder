package ru.artv.bk.studentproject.dao;

import org.junit.BeforeClass;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

public class DBInit {
    public static void initDB() throws URISyntaxException, IOException {
    URL resource1 = DictionaryDaoImplTest.class.getClassLoader().getResource("student_tables.sql");
    URL resource2 = DictionaryDaoImplTest.class.getClassLoader().getResource("student_data.sql");
    List<String> strings1 = Files.readAllLines(Paths.get(resource1.toURI()));
    List<String> strings2 = Files.readAllLines(Paths.get(resource2.toURI()));
    String data1 = strings1.stream().collect(Collectors.joining());
    String data2 = strings2.stream().collect(Collectors.joining());

    String data = data1+data2;

    try (Connection con = ConnectionBuilder.getConnection(); Statement stm = con.createStatement()) {
        stm.executeUpdate(data);
    } catch (SQLException e) {
        e.printStackTrace();
    }

}

}
