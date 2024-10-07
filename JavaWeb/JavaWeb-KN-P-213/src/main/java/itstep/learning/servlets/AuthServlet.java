package itstep.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.AuthDao;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.Base64;

@Singleton
public class AuthServlet extends HttpServlet {
    private final AuthDao authDao;

    @Inject
    public AuthServlet(AuthDao authDao) {
        this.authDao = authDao;
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
            restResponce.setStatus("success");
            restResponce.setCode(200);
            restResponce.setData(decodedCredentials);

        }
        catch (ParseException ex){
            restResponce.setStatus("error");
            restResponce.setCode(ex.getErrorOffset());
            restResponce.setData(ex.getMessage());

        }


        Gson gson = new GsonBuilder().serializeNulls().create();
        resp.setContentType("application/json");
        resp.getWriter().print(gson.toJson(restResponce));
    }

    class RestResponce {
        private int code;
        private String status;
        private Object data;

        public RestResponce() {

        }

        public RestResponce(int code, String status, Object data) {
            this.code = code;
            this.status = status;
            this.data = data;
        }
        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }

        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public Object getData() {
            return data;
        }
        public void setData(Object data) {
            this.data = data;
        }
    }
}

/*

Д.3. Створити сторінку для автоматизованого тестування API
У кодах сторінки надсилаються різні запити на /auth
як правильні, так і такі, що містять помилки
і виводяться відповіді на них

Without 'Authorization' header: {code: 401, status: 'error', data: 'Authorization header not found'
With non-Basic scheme: { ... }

** Відповіді, що відповідають очікуванням, позначати зеленим кольором, інші - червоним
*/
