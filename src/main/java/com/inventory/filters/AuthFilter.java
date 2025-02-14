package com.inventory.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI();

        String username = null;
        if (request.getSession(false) != null) {
            username = (String) request.getSession(false).getAttribute("username");
        }


        // Allow open paths like the login and static resources
        if (path.endsWith("login.jsp") || path.contains("/api/auth/") || path.contains("unauth.jsp")) {
            chain.doFilter(req, res); // Allow the request
            return;
        }

        // If the user is not logged in, redirect to the login page
        if (username == null) {
//
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.getWriter().write("{\"message\": \"not signed in.\"}");


            response.sendRedirect("login.jsp");

//            handleUnauthenticated(request, response);
            return;
        }

        // Allow access to all pages and APIs for "admin" user
        if ("admin".equals(username)) {
            chain.doFilter(req, res); // Proceed with request
            return;
        }

        // Allow access only to items.jsp and home.jsp for "user" username
       else {
            if (path.endsWith("/items.jsp") || path.endsWith("/home.jsp")) {
                chain.doFilter(req, res);
                return;
            } else {
                handleUnauthenticated(request, response);
//                response.sendRedirect("unauth.jsp");
                // Redirect to an unauthorized page if trying to access other pages
//
                return;
            }
        }

        // For any other user, redirect to the login page
//
    }

    private void handleUnauthenticated(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (isApiRequest(request)) {
            // Respond with JSON for API requests
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
            response.getWriter().write("{\"message\": \"dont have access.\"}");
        } else {
            // Redirect to login page for non-API requests
            response.sendRedirect("unauth.jsp");
        }
    }

    private boolean isApiRequest(HttpServletRequest request) {
        // Assume API requests start with "/api/"
        return request.getRequestURI().contains("/api/");
    }

    @Override
    public void destroy() {}
}
