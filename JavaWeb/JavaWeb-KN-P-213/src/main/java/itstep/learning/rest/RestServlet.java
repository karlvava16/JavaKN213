package itstep.learning.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RestServlet extends HttpServlet {
    protected   RestResponce restResponce;
    private HttpServletResponse response;
    private final Gson gson = new GsonBuilder().serializeNulls().create();


    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        response=resp;
        super.service(req, resp);
    }

    protected void sendResponce(int code, Object data, int maxAge) throws IOException
    {
        if(code > 0)
        {
            restResponce.setStatus(new RestStatus(code));
        }
        if(data != null)
            restResponce.setData(data);

        response.setContentType("application/json");
        
        response.setHeader("Cache-Control", maxAge > 0 ? "max-age=" + maxAge : "no-cache");

        response.getWriter().print(gson.toJson(restResponce));
    }

    protected void sendResponce(int code, Object data) throws IOException
    {
        this.sendResponce(code, data, 0);
    }

    protected void sendResponce(Object data) throws IOException
    {
        if( restResponce.getStatus() == null ) {
            this.sendResponce(200, data);
        }

        else {
            this.sendResponce(-1, data);
        }
    }

    protected void sendResponce(int code) throws IOException
    {
        this.sendResponce(code, null);
    }

    protected void sendResponce() throws IOException
    {
        if(restResponce.getStatus() == null)
        {
            this.sendResponce(200);
        }
        else
        {
            this.sendResponce(-1);
        }
    }
}
