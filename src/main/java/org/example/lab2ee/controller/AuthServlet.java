package org.example.lab2ee.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.lab2ee.model.User;
import org.example.lab2ee.service.ServiceFactory;
import org.example.lab2ee.service.UserService;

import java.io.IOException;
import java.util.Optional;


@WebServlet({"/login", "/logout"})
public class AuthServlet extends HttpServlet {

    public static final String SESSION_USER_KEY = "currentUser";

    @EJB
    private UserService userService;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String uri = req.getServletPath();

        if ("/logout".equals(uri)) {
            HttpSession session = req.getSession(false);
            if (session != null) session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/login?msg=logged_out");
            return;
        }

        // If already logged in redirect to home
        if (req.getSession(false) != null &&
                req.getSession(false).getAttribute(SESSION_USER_KEY) != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }

        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }

    // ── POST /login → authenticate ────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String username = sanitize(req.getParameter("username"));
        String password = req.getParameter("password"); // not sanitised — checked as-is

        Optional<User> result = userService.authenticate(username, password);

        if (result.isEmpty()) {
            req.setAttribute("error", "Невірний логін або пароль.");
            req.setAttribute("username", username); // repopulate field
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        User user = result.get();

        // Invalidate old session (session fixation protection)
        HttpSession oldSession = req.getSession(false);
        if (oldSession != null) oldSession.invalidate();

        HttpSession session = req.getSession(true);
        session.setAttribute(SESSION_USER_KEY, user);
        session.setMaxInactiveInterval(30 * 60); // 30 min

        // Redirect based on role
        String redirectUrl = req.getContextPath() + (user.isAdmin() ? "/admin/menu" : "/menu");
        resp.sendRedirect(redirectUrl);
    }

    private String sanitize(String input) {
        if (input == null) return "";
        return input.trim()
                .replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;");
    }
}
