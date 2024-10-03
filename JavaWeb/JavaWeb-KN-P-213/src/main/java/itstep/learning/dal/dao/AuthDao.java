package itstep.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.services.db.DbService;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

@Singleton
public class AuthDao {
    private final DbService dbService;
    private final Logger logger;

    @Inject
    public AuthDao(DbService dbService, Logger logger) {
        this.dbService = dbService;
        this.logger = logger;
    }

    public boolean install()
    {
        String sql = "CREATE TABLE IF NOT EXISTS `users` (" +
                " `user_id` CHAR(36) PRIMARY KEY DEFAULT ( UUID() )," +
                " `user_name`   VARCHAR(64)          NOT NULL," +
                " `email`       VARCHAR(128)         NOT NULL," +
                " `phone`       VARCHAR(16)          NULL," +
                " `avatar_url`  VARCHAR(128)         NOT NULL," +
                " `birthdate`   DATETIME             NOT NULL," +
                " `delete_dt`   DATETIME             NULL" +
                ") ENGINE=InnoDB default CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";

        try(Statement stmt = dbService.getConnection().createStatement())
        {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
                logger.warning(ex.getMessage() + "--" + sql);
                return false;
        }
        return true;
    }
}


/*

DAO - Data Access Object
набір інструментів (бізнес-логіка) для роботи з
DTO - Data Transfer Object (Entities) - моделями
передачі даних

Задачі авторизації / автентифікації


[users]
|user_id
|name
|email
|phone
|avatar_url
|birthdate
|delete_dt



[users_access]
|access_id
|user_id
|login
|salt
|dk

[users_roles]
|role_id
|name
|can_create
|can_read
|can_update
|can_delete

[tokens]
|token_id
|user_id
|iat
|exp

[users_details]
|user_id
|tg_url
|fb_url
Iwork_email
|work_phone
|work_address
|home_address


Д.З. Реалізувати сервіс для генерування випадкових імен файлів
(без розширення) - випадковий набір символів одного реєстру
у якому немає активних символів файлової системи (./*?\...)
Сервіс може приймати параметр - довжина імені (у символах),
якщо не передається, то вживає дані за замовчанням (з ентропією 64 біти)
Інжектувати до домашньої сторінки, вивести пробні результати
різної довжини
 */
