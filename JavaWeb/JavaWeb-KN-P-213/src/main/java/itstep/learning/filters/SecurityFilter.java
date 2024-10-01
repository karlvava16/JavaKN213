package itstep.learning.filters;

import javax.servlet.*;
import java.io.IOException;

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
