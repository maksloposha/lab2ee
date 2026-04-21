package org.example.lab2ee.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.lab2ee.model.Order;
import org.example.lab2ee.service.OrderService;
import org.example.lab2ee.service.ServiceFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@WebServlet("/admin/orders/*")
public class AdminOrderServlet extends HttpServlet {

    private OrderService orderService;

    @Override
    public void init() {
        orderService = ServiceFactory.getOrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "/";
        switch (path) {
            case "/view":
                viewOrder(req, resp);
                break;
            default:
                listOrders(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();
        if ("/status".equals(path)) updateStatus(req, resp);
        else resp.sendRedirect(req.getContextPath() + "/admin/orders");
    }

    private void listOrders(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String statusParam = req.getParameter("status");
        List<Order> orders;
        Order.Status filterStatus = null;
        if (statusParam != null && !statusParam.isEmpty()) {
            try {
                filterStatus = Order.Status.valueOf(statusParam.toUpperCase());
                orders = orderService.getOrdersByStatus(filterStatus);
            } catch (IllegalArgumentException e) {
                orders = orderService.getAllOrders();
            }
        } else {
            orders = orderService.getAllOrders();
        }
        req.setAttribute("orders", orders);
        req.setAttribute("allStatuses", Order.Status.values());
        req.setAttribute("filterStatus", filterStatus);
        req.getRequestDispatcher("/WEB-INF/views/admin/order-list.jsp").forward(req, resp);
    }

    private void viewOrder(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = parseIntOrDefault(req.getParameter("id"), 0);
        Optional<Order> order = orderService.getOrderById(id);
        if (order.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/admin/orders?error=not_found");
            return;
        }
        req.setAttribute("order", order.get());
        req.setAttribute("allStatuses", Order.Status.values());
        req.getRequestDispatcher("/WEB-INF/views/admin/order-detail.jsp").forward(req, resp);
    }

    private void updateStatus(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int id = parseIntOrDefault(req.getParameter("orderId"), 0);
        try {
            Order.Status newStatus = Order.Status.valueOf(req.getParameter("status"));
            orderService.updateOrderStatus(id, newStatus);
            resp.sendRedirect(req.getContextPath() + "/admin/orders/view?id=" + id + "&success=status_updated");
        } catch (Exception e) {
            resp.sendRedirect(req.getContextPath() + "/admin/orders?error=update_failed");
        }
    }

    private int parseIntOrDefault(String v, int def) {
        try {
            return Integer.parseInt(v);
        } catch (Exception e) {
            return def;
        }
    }
}
