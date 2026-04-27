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
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet("/admin/menu/*")
public class AdminMenuServlet extends HttpServlet {

    @EJB
    private MenuService menuService;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "/";
        switch (path) {
            case "/new":
                showNewForm(req, resp);
                break;
            case "/edit":
                showEditForm(req, resp);
                break;
            default:
                listMenuItems(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();
        if (path == null) path = "/";
        switch (path) {
            case "/create":
                createMenuItem(req, resp);
                break;
            case "/update":
                updateMenuItem(req, resp);
                break;
            case "/delete":
                deleteMenuItem(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/admin/menu");
        }
    }

    private void listMenuItems(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("menuItems", menuService.getAllMenuItems());
        req.setAttribute("categories", menuService.getAllCategories());
        req.getRequestDispatcher("/WEB-INF/views/admin/menu-list.jsp").forward(req, resp);
    }

    private void showNewForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("categories", menuService.getAllCategories());
        req.setAttribute("formAction", req.getContextPath() + "/admin/menu/create");
        req.setAttribute("pageTitle", "Новий елемент меню");
        req.getRequestDispatcher("/WEB-INF/views/admin/menu-form.jsp").forward(req, resp);
    }

    private void showEditForm(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = parseIntOrDefault(req.getParameter("id"), 0);
        Optional<MenuItem> item = menuService.getMenuItemById(id);
        if (item.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/menu?error=not_found");
            return;
        }
        req.setAttribute("item", item.get());
        req.setAttribute("categories", menuService.getAllCategories());
        req.setAttribute("formAction", req.getContextPath() + "/admin/menu/update");
        req.setAttribute("pageTitle", "Редагування елемента меню");
        req.getRequestDispatcher("/WEB-INF/views/admin/menu-form.jsp").forward(req, resp);
    }

    private void createMenuItem(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        MenuItem item = buildItemFromRequest(req, new MenuItem());
        menuService.createMenuItem(item);
        resp.sendRedirect(req.getContextPath() + "/admin/menu?success=created");
    }

    private void updateMenuItem(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = parseIntOrDefault(req.getParameter("id"), 0);
        Optional<MenuItem> existing = menuService.getMenuItemById(id);
        if (existing.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/menu?error=not_found");
            return;
        }
        MenuItem item = buildItemFromRequest(req, existing.get());
        menuService.updateMenuItem(item);
        resp.sendRedirect(req.getContextPath() + "/admin/menu?success=updated");
    }

    private void deleteMenuItem(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        menuService.deleteMenuItem(parseIntOrDefault(req.getParameter("id"), 0));
        resp.sendRedirect(req.getContextPath() + "/admin/menu?success=deleted");
    }

    private MenuItem buildItemFromRequest(HttpServletRequest req, MenuItem item) {
        item.setName(sanitize(req.getParameter("name")));
        item.setDescription(sanitize(req.getParameter("description")));
        item.setAvailable("on".equals(req.getParameter("available")));
        item.setCalories(parseIntOrDefault(req.getParameter("calories"), 0));
        try {
            item.setPrice(new BigDecimal(req.getParameter("price")));
        } catch (Exception e) {
            item.setPrice(BigDecimal.ZERO);
        }
        try {
            item.setCategory(MenuItem.Category.valueOf(req.getParameter("category")));
        } catch (Exception e) {
            item.setCategory(MenuItem.Category.MAIN_COURSE);
        }
        return item;
    }

    private int parseIntOrDefault(String v, int def) {
        try {
            return Integer.parseInt(v);
        } catch (Exception e) {
            return def;
        }
    }

    private String sanitize(String input) {
        if (input == null) return "";
        return input.trim().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;");
    }
}
