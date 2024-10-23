package itstep.learning.dal.dao.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dto.User;
import itstep.learning.dal.dto.shop.Cart;
import itstep.learning.dal.dto.shop.CartItem;
import itstep.learning.dal.dto.shop.Product;
import itstep.learning.services.db.DbService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Singleton
public class CartDao {
    private final DbService dbService;
    private final Logger logger;

    @Inject
    public CartDao(DbService dbService, Logger logger) {

        this.dbService = dbService;
        this.logger = logger;
    }

    public boolean install() {
        String sql = "CREATE TABLE IF NOT EXISTS `carts` (" +
                " `cart_id` CHAR(36) PRIMARY KEY DEFAULT (UUID())," +
                " `user_id` CHAR(36) NOT NULL," +
                " `cart_create_dt` DATETIME NOT NULL," +
                " `cart_close_dt` DATETIME NULL," +
                " `cart_status` INT DEFAULT 1" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";

        try (Statement stmt = dbService.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            logger.warning(ex.getMessage() + "--" + sql);
            return false;
        }

        sql = "CREATE TABLE IF NOT EXISTS `cart_items` (" +
                " `cart_id` CHAR(36) NOT NULL," +
                " `product_id` CHAR(36) NOT NULL," +
                " `cart_item_quantity` INT DEFAULT 1," +
                " `cart_item_price` DECIMAL(8,2) NOT NULL," +
                " PRIMARY KEY (`cart_id`, `product_id`)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci";

        try (Statement stmt = dbService.getConnection().createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException ex) {
            logger.warning(ex.getMessage() + "--" + sql);
            return false;
        }

        return true;
    }


    public boolean add(User user, Product product) {
        if (user == null || product == null) {
            return false;
        }
        Cart cart = getCartByUser(user);
        if (cart == null) {
            cart = openCartByUser(user);
            if (cart == null) {
                return false;
            }
        }

        int quantity = -1;
        String sql = "SELECT ci.cart_item_quantity FROM cart_items ci WHERE ci.cart_id = ? AND ci.product_id = ?";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, cart.getId().toString());
            prep.setString(2, product.getId().toString());
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                quantity = rs.getInt(1);
            }
        } catch (SQLException ex) {
            logger.warning(ex.getMessage() + "--" + sql);
            return false;
        }

        if (quantity == -1) {
            sql = "INSERT INTO cart_items (cart_item_quantity, cart_item_price, cart_id, product_id) " +
                    "VALUES (1, ?, ?, ?)";
        } else {
            sql = "UPDATE cart_items " +
                    "SET cart_item_quantity = cart_item_quantity + 1, cart_item_price = cart_item_price + ? " +
                    "WHERE cart_id = ? AND product_id = ?";
        }
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setDouble(1, product.getPrice());
            prep.setString(2, cart.getId().toString());
            prep.setString(3, product.getId().toString());
            prep.executeUpdate();
            return true;
        } catch (SQLException ex) {
            logger.warning(ex.getMessage() + "--" + sql);
            return false;
        }

    }

    public boolean update(String cartId, String productId, int increment) {
        // TODO: перевірити чи increment не призведе до від'ємних значень у кількості
        String sql = "UPDATE cart_items " +
                "SET cart_item_quantity = cart_item_quantity + ?, " +
                "cart_item_price = cart_item_price + ? " +
                "* (SELECT product_price FROM products WHERE product_id = ?) " +
                "WHERE cart_id = ? AND product_id = ?";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setInt(1, increment);
            prep.setInt(2, increment);
            prep.setString(3, productId);
            prep.setString(4, cartId);
            prep.setString(5, productId);
            prep.executeUpdate();
            return true;

        } catch (SQLException ex) {
            logger.warning(ex.getMessage() + "--" + sql);
        }
        return false;
    }

    public Cart openCartByUser(User user) {
        if (user == null) {
            return null;
        }
        Cart cart = new Cart();
        cart.setId(UUID.randomUUID());
        cart.setUserId(user.getUserId());
        cart.setUser(user);
        cart.setCreateDt(new Date());
        cart.setCloseDt(null);
        cart.setStatus(0);
        String sql = "INSERT INTO carts (cart_id, user_id, cart_create_dt, cart_close_dt, cart_status)" +
                "VALUES(?, ?, ?, ?, ?)";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, cart.getId().toString());
            prep.setString(2, cart.getUserId().toString());
            prep.setTimestamp(3, new Timestamp(cart.getCreateDt().getTime()));
            if (cart.getCloseDt() != null) {
                prep.setTimestamp(4, new Timestamp(cart.getCloseDt().getTime()));
            } else {
                prep.setTimestamp(4, null);
            }
            prep.setInt(5, cart.getStatus());
            prep.executeUpdate();
            return cart;
        } catch (SQLException ex) {
            logger.warning(ex.getMessage() + "--" + sql);
        }
        return null;

    }


    public Cart getCartByUser(User user) {
        return getCartByUser(user, false);
    }

    public Cart getCartByUser(User user, boolean withItems) {
        Cart cart = null;
        String sql = "SELECT * FROM carts c WHERE c.user_id = ? AND c.cart_close_dt IS NULL";
        try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
            prep.setString(1, user.getUserId().toString());
            ResultSet rs = prep.executeQuery();
            if (rs.next()) {
                cart = new Cart(rs);
            }
            rs.close();
        } catch (SQLException ex) {
            logger.warning(ex.getMessage() + "--" + sql);
            return null;
        }
        if (cart != null && withItems) {
            List<CartItem> cartItems = new ArrayList<>();
            sql = "SELECT * FROM cart_items ci JOIN products p ON ci.product_id = p.product_id WHERE ci.cart_id = ?";
            try (PreparedStatement prep = dbService.getConnection().prepareStatement(sql)) {
                prep.setString(1, cart.getId().toString());
                ResultSet rs = prep.executeQuery();
                while (rs.next()) {
                    cartItems.add(new CartItem(rs));
                }
                rs.close();
                cart.setCartItems(cartItems.toArray(new CartItem[0]));
            } catch (SQLException ex) {
                logger.warning(ex.getMessage() + "--" + sql);
                return null;
            }
        }
        return cart;

    }
}

/*
[carts]
|cart_id
user_id
cart_create_dt
cart_close_dt
cart_status(0,1,-1)

[cart_items]
|cart_id
|product_id
cart_item_quantity
cart_item_price

*/
