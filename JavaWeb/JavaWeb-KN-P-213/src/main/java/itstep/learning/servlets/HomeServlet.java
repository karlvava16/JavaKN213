package itstep.learning.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("")
public class HomeServlet extends HttpServlet {
    @Override
    protected  void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        req.setAttribute("body", "home.jsp"); // ViewData["body"] = "home.jsp   "
        if(  req.getAttribute("signature") != null) {
            req.setAttribute("body", "home.jsp"); // ~ Vi
        }
        else{
                req.setAttribute("body", "not_found.jsp");
            }
        // ~ return View();
        req.getRequestDispatcher("WEB-INF/views/_layout.jsp").forward(req, resp);
        //resp.getWriter().println("<h1>Home</h1>");
    }

}

/*



*/

