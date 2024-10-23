package itstep.learning.servlets.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.shop.CartDao;
import itstep.learning.dal.dao.shop.ProductDao;
import itstep.learning.dal.dto.User;
import itstep.learning.dal.dto.shop.CartItem;
import itstep.learning.dal.dto.shop.Product;
import itstep.learning.rest.RestMetaData;
import itstep.learning.rest.RestResponse;
import itstep.learning.rest.RestServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Singleton
public class CartServlet extends RestServlet {
    private final CartDao cartDao;
    private final ProductDao productDao;

    @Inject
    public CartServlet(CartDao cartDao, ProductDao productDao) {
        this.cartDao = cartDao;
        this.productDao = productDao;
    }


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.restResponse = new RestResponse().setMeta(
                new RestMetaData()
                        .setUrl("/shop/cart")
                        .setMethod((req.getMethod()))
                        .setName("KN-P-213 Shop API for carts")
                        .setServerTime(new Date())
                        .setAllowedMethods(new String[]{"GET", "POST", "PUT", "DELETE", "OPTIONS"})
        );

        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getAttribute("auth-token-user");
        if (user != null) {
            super.sendResponse(200, cartDao.getCartByUser(user, true));
        }
        else
        {
            super.sendResponse(401, new CartItem[0]);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // додати товар до кошику (CREATE)
        // 1. Встановити користувача (за токеном)
        // 1.1. Якщо приходить запит від неавторизованого користувача, то ...
        // 2. Дізнатись чи є в користувача відкритий кошик
        //   якщо е, то додаємо до нього,
         //       якщо немає, то створюемо новий
        // 3. Перевіряємо чи є зазначений товар у кошику
       // якщо є - збільшуємо кількість
       // якщо немає - додаємо

        String productId = req.getParameter( "product-id" );
        if( productId == null || productId.isEmpty() ){
            super. sendResponse( 400, "Missing required parameter 'product-id'" );
            return;
        }
        Product product = productDao.getByIdOrSlug( productId );
        if( product == null ) {
            super.sendResponse(404, "Product not found");
            return;
        }
        User user = (User) req.getAttribute("auth-token-user");
        if(user != null) {
            if (cartDao.add(user, product))
            {
                super.sendResponse( 201, productId );
                return;
            }
            else
            {
                super.sendResponse( 500, "Error adding product" );
            }
        }
        else
        {
            super.sendResponse(401);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getAttribute("auth-token-user");
        if (user == null) {
            super.sendResponse(401, null);
            return;
        }
        String cartId = req.getParameter( "cart-id" );
        if( cartId == null || cartId.isEmpty() ){
            super.sendResponse(400, "Missing required parameter 'cart-id'" );
            return;
        }
        String productId = req.getParameter( "product-id" );
        if( productId == null || productId.isEmpty() ){
            super.sendResponse(400, "Missing required parameter 'product-id'" );
            return;
        }

        int increment;
        String delta = req.getParameter( "delta" );
        if( delta == null || delta.isEmpty() ){
            increment = 1;
        }
        else {
            try
            {
                increment = Integer.parseInt( delta );
            }
            catch ( NumberFormatException ignored )
            {
                super.sendResponse(400, "Invalid number format param 'delta'" );
                return;
            }
        }
        if (cartDao.update(cartId,productId, increment))
        {
            super.sendResponse( 202, "Update" );
        }
        else {
            super.sendResponse( 500, "Error updating product" );
        }
    }
}
