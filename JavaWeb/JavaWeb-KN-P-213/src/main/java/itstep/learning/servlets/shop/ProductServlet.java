package itstep.learning.servlets.shop;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.shop.ProductDao;
import itstep.learning.dal.dto.shop.Product;
import itstep.learning.rest.RestMetaData;
import itstep.learning.rest.RestResponse;
import itstep.learning.rest.RestServlet;
import itstep.learning.services.form.FormParseResult;
import itstep.learning.services.form.FormParseService;
import itstep.learning.services.storage.StorageService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

@Singleton
public class ProductServlet extends RestServlet {
    private final Logger logger;
    private final FormParseService formParseService;
    private final StorageService storageService;
    private final ProductDao productDao;

    @Inject
    public ProductServlet(Logger logger, FormParseService formParseService, StorageService storageService, ProductDao productDao) {
        this.logger = logger;
        this.formParseService = formParseService;
        this.storageService = storageService;
        this.productDao = productDao;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.restResponse = new RestResponse().setMeta(
                new RestMetaData()
                        .setUrl("/shop/product")
                        .setMethod((req.getMethod()))
                        .setName("KN-P-213 Shop API for products")
                        .setServerTime(new Date())
                        .setAllowedMethods(new String[]{"GET", "POST", "PUT", "DELETE", "OPTIONS"})
        );

        super.service(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String id = req.getParameter("id");
        if( id != null ) {
            this.getById( id );
            return;
        }
        String category = req.getParameter("category");
        if( category != null ) {
            this.getByCategory( category );
            return;
        }
        super. sendResponse( 400, "Missing required field: 'category' or 'id' " );

    }

    private void getById( String id) throws IOException {
        super.sendResponse( 200, productDao.getByIdOrSlug( id, true ) );
    }

    private void getByCategory( String category) throws IOException {
        super.sendResponse( 200, productDao.read( category ) );
    }



    @Override // CREATE - створити нову категорію товарів
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Product product;
        try {
            product = parseProduct(req);
        } catch (Exception ex) {
            super.sendResponse(400, ex.getMessage());
            return;
        }

        if ((product = productDao.create(product)) != null) {
            super.sendResponse(200, product);
        } else {
            super.sendResponse(500, "Error creating product");
        }
    }



    private Product parseProduct(HttpServletRequest req) throws Exception {
        FormParseResult formParseResult = formParseService.parse(req);
        String data = formParseResult.getFields().get("product-name");
        Product product = new Product();
        if (data == null || data.isEmpty()) {
            throw new Exception( "Missing required field 'product-name' ");
        }

        product.setName(data);

        data = formParseResult.getFields().get("product-description");
        if (data == null || data.isEmpty()) {
            throw new Exception( "Missing required field 'product-description' ");

        }

        product.setDescription(data);

        data = formParseResult.getFields().get("category-id");
        if (data == null || data.isEmpty()) {
            throw new Exception( "Missing required field 'category-id' ");

        }
        try {
            product.setCategoryId(UUID.fromString(data));
        } catch (IllegalArgumentException ignored) {
            throw new Exception( "Required 'category-id' has invalid format" );
        }

        data = formParseResult.getFields().get("product-price");
        if (data == null || data.isEmpty()) {
            throw new Exception( "Missing required field 'product-price' ");

        }
        try {
            product.setPrice(Double.parseDouble(data));
        } catch (IllegalArgumentException ignored) {
            throw new Exception( "Required 'product-price' has invalid format" );
        }

        data = formParseResult.getFields().get("product-quantity");
        if (data == null || data.isEmpty()) {
            product.setQuantity(1);
        }
        else try {
            product.setQuantity(Integer.parseInt(data));
        } catch (IllegalArgumentException ignored) {
            throw new Exception( "Required 'product-quantity' has invalid format" );
        }


        data = formParseResult.getFields().get("product-slug");
        // правило Де Моргана
        if (data != null && !data.isEmpty()) {
            if (!productDao.isSlugFree(data)) {
                throw new Exception( "Value of 'product-slug' is not free");

            }
            product.setSlug(data);

        }



        try {
            data = storageService.saveFile(
                    formParseResult.getFiles().get("product-image"));
        } catch (IOException ex) {
            logger.warning(ex.getMessage());
            super.sendResponse(400, "Missing required field 'product-image' ");

        }
        product.setImageUrl(data);

       return product;
    }
}
