package itstep.learning.dal.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.services.db.DbService;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

@Singleton
public class ProductDao {
    private final DbService dbService;
    private final Logger logger;

    @Inject
    public ProductDao(DbService dbService, Logger logger) {
        this.dbService = dbService;
        this.logger = logger;
    }

    public boolean install()
    {
        String sql = "CREATE TABLE IF NOT EXISTS `products` (" +
                " `product_id`          CHAR(36)          PRIMARY KEY DEFAULT ( UUID() )," +
                " `product_name`        VARCHAR(64)       NOT NULL," +
                " `product_description` TEXT              NOT NULL," +
                " `product_price`       DECIMAL(8,2)       NULL," +
                " `product_img_url`     VARCHAR(256)      NULL," +  
                " `product_amount`      INT               NOT NULL," +
                " `product_delete_dt`   DATETIME          NULL" +
                " `product_slug`  VARCHAR(256)         NULL UNIQUE," +
                ") ENGINE=InnoDB default CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";

        try(Statement stmt = dbService.getConnection().createStatement())
        {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            logger.warning(ex.getMessage() + "--" + sql);
            return false;
        }


         sql = "CREATE TABLE IF NOT EXISTS `products_images` (" +
                " `product_id`          CHAR(36)    NOT NULL," +
                " `product_img_url`     VARCHAR(256)     NOT NULL," +
                 "PRIMARY KEY(`product_id`, `product_img_url`)" +
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
