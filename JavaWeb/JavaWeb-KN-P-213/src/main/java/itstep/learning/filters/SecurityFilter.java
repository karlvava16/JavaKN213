package itstep.learning.filters;

import javax.inject.Singleton;
import javax.servlet.*;
import java.io.IOException;

@Singleton
public class SecurityFilter implements Filter {
    private FilterConfig filterConfig;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        servletRequest.setAttribute("signature", true);
        filterChain.doFilter(servletRequest, servletResponse);

    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}


/*
Розробити файл, що відповідає порушенню безпечного доступу
insecure.jsp
Переводити на цей файл, якщо у запиті відсутній підпис від
відповідного фільтру SecurityFilter ("signature") або
його значення не відповідає очікуваному (true)
*/