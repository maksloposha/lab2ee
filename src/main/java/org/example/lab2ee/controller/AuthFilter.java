package org.example.lab2ee.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.lab2ee.model.User;

import java.io.IOException;


@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String path = req.getServletPath();
        if (req.getPathInfo() != null) path += req.getPathInfo();

        // Always allow: static resources, login/logout, REST API, public pages
        if (isPublic(path)) {
            chain.doFilter(request, response);
            return;
        }

        // Get current user from session
        HttpSession session = req.getSession(false);
        User user = (session != null)
                ? (User) session.getAttribute(AuthServlet.SESSION_USER_KEY)
                : null;

        // ── /admin/* — requires ADMIN role ───────────────────────────────────
        if (path.startsWith("/admin")) {
            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/login?redirect=" +
                        java.net.URLEncoder.encode(path, "UTF-8"));
                return;
            }
            if (!user.isAdmin()) {
                req.setAttribute("errorCode", "403");
                req.setAttribute("errorMessage", "Доступ заборонено. Ця сторінка лише для адміністраторів.");
                req.getRequestDispatcher("/WEB-INF/views/error.jsp").forward(req, resp);
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        // ── /order/* — requires any authenticated user ────────────────────────
        if (path.startsWith("/order") || path.startsWith("/my-orders")) {
            if (user == null) {
                resp.sendRedirect(req.getContextPath() + "/login?redirect=" +
                        java.net.URLEncoder.encode(path, "UTF-8"));
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        // All other paths are public
        chain.doFilter(request, response);
    }

    private boolean isPublic(String path) {
        return path.startsWith("/api")
                || path.startsWith("/login")
                || path.startsWith("/logout")
                || path.equals("/")
                || path.startsWith("/menu")
                || path.startsWith("/css")
                || path.startsWith("/js")
                || path.startsWith("/images")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".ico");
    }

    @Override
    public void init(FilterConfig cfg) {
    }

    @Override
    public void destroy() {
    }
}
