package itstep.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.AuthDao;
import itstep.learning.dal.dto.User;
import itstep.learning.models.SignupFormModel;
import itstep.learning.rest.RestMetaData;
import itstep.learning.rest.RestResponce;
import itstep.learning.rest.RestServlet;
import itstep.learning.rest.RestStatus;
import itstep.learning.services.form.FormParseResult;
import itstep.learning.services.form.FormParseService;
import itstep.learning.services.storage.StorageService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;
import java.util.Date;

@Singleton
public class AuthServlet extends RestServlet {
    private final AuthDao authDao;
    private final FormParseService formParseService;
    private final StorageService storageService;

    @Inject
    public AuthServlet(AuthDao authDao, FormParseService formParseService, StorageService storageService) {
        this.authDao = authDao;
        this.formParseService = formParseService;
        this.storageService = storageService;
    }

    @Override
    protected  void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        /*
        The 'Basic' HTTP Authentication Scheme
        https://datatracker.ietf.org/doc/html/rfc7617
        */
        // Вилучаємо заголовок Authorization
        // Перевіряємо,що схема Basic
        // Виділяємо дані автентифікації (credentials)
        // Декодуємо їх за Base64
        // Розділяємо за першим символом ':'
        // запитаємо автентифікацію в DAO
        RestResponce restResponce = new RestResponce();
        try
        {
            String authHeader = req.getHeader("Authorization");
            if (authHeader == null)
            {
                throw new ParseException("Authorization header not found", 401);
            }

            String authScheme = "Basic ";
            if (! authHeader.startsWith(authScheme)) {
                throw new ParseException("Invalid Authorization scheme. Required " + authScheme, 400);
            }

            String credentials = authHeader.substring(authScheme.length());

            String decodedCredentials;
            try
            {
                decodedCredentials = new String(Base64.getDecoder().decode(credentials.getBytes(StandardCharsets.UTF_8)),
                        StandardCharsets.UTF_8
                );
            } catch (IllegalAccessError ex) {
                throw new ParseException("Invalid credentials format", 400);
            }

            String[] parts = decodedCredentials.split(":", 2);
            if (parts.length != 2)
            {
                throw new ParseException("Invalid credentials composition", 400);
            }

            User user = authDao.authenticate(parts[0], parts[1]);
            if (user == null)
            {
                throw new ParseException("Credentials rejected", 401);
            }

            super.sendResponce( user );


        }
        catch (ParseException ex){
            super.sendResponce( ex.getErrorOffset(), ex. getMessage() );
        }

    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.restResponce = new RestResponce().setMeta(
                new RestMetaData()
                        .setUrl("/auth")
                        .setMethod((req.getMethod()))
                        . setName ( "KN-P-213 Authentication API" )
                        . setServerTime( new Date() )
                        .setAllowedMethods(new String[]{"GET", "POST", "PUT", "DELETE", "OPTIONS"})
        );

        super.service(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // Sign Up
        // req.getParameter("name") - параметри запиту: URL - або form-дані
        // АЛЕ! за умови, що форма передається як х-www-form-urlencoded
        // і не працює для multipart/form-data
        FormParseResult formParseResult = formParseService.parse( req );
        String savedName;
        try {
            savedName = storageService.saveFile(
                    formParseResult.getFiles().get("signup-avatar") );
        }
        catch( IOException ex ) {
            savedName = ex.getMessage();
        }
        super.sendResponce(
                "files: " + formParseResult.getFiles().size() +
                        ", fields: " + formParseResult.getFields().size() +
                        ", name: " + savedName
        );
    }
    //TODO: реалізувати
    private SignupFormModel getSignupFormModel(HttpServletRequest req) throws Exception {
        return null;
    }

    /*

Д.3. Реалізувати сервіси парсингу форм (FormParseService)
та управління збереженням/видачею файлів (StorageService)
у власних курсових проєктах
*/
}

/*
application/x-www-form-urlencoded

HTTP/1.1 200 0K
Content-Type: application/x-www-form-urlencoded

name=User&password=123

*/

/*
HTTP/1.1 200 0K
Content-Type: multipart/form-data; delimiter=asdf...w
--asdf...w

Content-Disposition: form-field; name=user-name

user
--asdf...w
Content-Disposition: form-field; name=user-avatar

123
--asdf ..w
Content-Disposition: form-file; name=user-avatar

PNGd45sf4ghsem8jz'dflhgsd4f
Gzz;jd'
--asdf...w--

*/