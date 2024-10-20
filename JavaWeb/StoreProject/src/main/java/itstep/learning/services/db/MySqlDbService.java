package itstep.learning.services.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlDbService implements DbService {

    private Connection connection;

    @Override
    public Connection getConnection() throws SQLException {
        if (connection == null)
        {
            // процес підключення: реєструємо драйвер СУБД (MySQL)
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
            // формуємо рядок підключення
            String connectionUrl = "jdbc:mysql://localhost:3308/store_project" +
                    "?useUnicode=true&characterEncoding=utf8"; // &useSSL=false
            String username = "karl";
            String password = "karlo";
            // одержуємо підключення
            connection = DriverManager.getConnection( connectionUrl, username, password );
        }
        return connection;
    }
}


/*
JDBC - Java DataBase Connectivity - технологія доступу до даних, аналогічна
до ADO.NET
Надає узагальнений інтерфейс для роботи з різними джерелами даних (СУБД)
Для роботи з конкретною СУБД необхідно встановити конектор (драйвер)
відповідної СУБД
*/