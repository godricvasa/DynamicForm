package main.webapp.Filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class FilterServlet implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpSession session = httpRequest.getSession(false);
        boolean isAjaxRequest = "XMLHttpRequest".equals(httpRequest.getHeader("X-Requested-With"));
        String path = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());



        boolean isPublicPage = path.equals("/login") || path.equals("/register");
        boolean loggedIn = (session != null && session.getAttribute("username") != null);

            if (!loggedIn && !isPublicPage) {
                System.out.println("illegal");
                // User not logged in and requesting a protected page
                if (isAjaxRequest) {
                    // For AJAX requests, send a 401 status

                    httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED);

                } else {
                    // For regular requests, redirect to login page
                    httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
                }
            } else if (loggedIn && (path.equals("/login")||path.equals("/"))) {
//                httpResponse.sendError(HttpServletResponse.SC_MOVED_PERMANENTLY);
                httpResponse.sendRedirect(httpRequest.getContextPath() + "/main");
            } else {
                chain.doFilter(request, response);
            }
        }



    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
