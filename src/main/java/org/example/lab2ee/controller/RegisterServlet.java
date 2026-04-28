package org.example.lab2ee.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.lab2ee.model.User;
import org.example.lab2ee.service.UserService;

import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    @EJB
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(AuthServlet.SESSION_USER_KEY) != null) {
            resp.sendRedirect(req.getContextPath() + "/");
            return;
        }
        req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");

        String username        = req.getParameter("username");
        String password        = req.getParameter("password");
        String passwordConfirm = req.getParameter("passwordConfirm");
        String fullName        = req.getParameter("fullName");
        String email           = req.getParameter("email");
        String phone           = req.getParameter("phone");

        if (password == null || !password.equals(passwordConfirm)) {
            req.setAttribute("error", "Паролі не збігаються.");
            repopulate(req, username, fullName, email, phone);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        try {
            User user = userService.register(username, password, fullName, email, phone);

            // Одразу логінимо після реєстрації
            HttpSession old = req.getSession(false);
            if (old != null) old.invalidate();
            HttpSession session = req.getSession(true);
            session.setAttribute(AuthServlet.SESSION_USER_KEY, user);
            session.setMaxInactiveInterval(30 * 60);

            resp.sendRedirect(req.getContextPath() + "/menu?registered=true");

        } catch (IllegalArgumentException e) {
            // Помилка валідації з сервісу — показуємо користувачу
            req.setAttribute("error", e.getMessage());
            repopulate(req, username, fullName, email, phone);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        }
    }

    private void repopulate(HttpServletRequest req, String username,
                            String fullName, String email, String phone) {
        req.setAttribute("username", username);
        req.setAttribute("fullName", fullName);
        req.setAttribute("email", email);
        req.setAttribute("phone", phone);
    }
}
