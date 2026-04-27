package org.example.lab2ee.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.lab2ee.model.MenuItem;
import org.example.lab2ee.service.MenuService;
import org.example.lab2ee.service.ServiceFactory;

import java.io.IOException;
import java.util.List;

@WebServlet("/menu")
public class MenuServlet extends HttpServlet {

    @EJB
    private MenuService menuService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String categoryParam = req.getParameter("category");
        List<MenuItem> items;
        MenuItem.Category selectedCategory = null;

        if (categoryParam != null && !categoryParam.isEmpty()) {
            try {
                selectedCategory = MenuItem.Category.valueOf(categoryParam.toUpperCase());
                items = menuService.getMenuItemsByCategory(selectedCategory);
            } catch (IllegalArgumentException e) {
                items = menuService.getAvailableMenuItems();
            }
        } else {
            items = menuService.getAvailableMenuItems();
        }

        req.setAttribute("menuItems", items);
        req.setAttribute("categories", menuService.getAllCategories());
        req.setAttribute("selectedCategory", selectedCategory);
        req.getRequestDispatcher("/WEB-INF/views/user/menu.jsp").forward(req, resp);
    }
}
