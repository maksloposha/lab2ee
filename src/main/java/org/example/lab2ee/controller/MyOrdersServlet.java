package org.example.lab2ee.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.lab2ee.model.Order;
import org.example.lab2ee.model.User;
import org.example.lab2ee.service.OrderService;
import org.example.lab2ee.service.ServiceFactory;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


@WebServlet("/my-orders/*")
public class MyOrdersServlet extends HttpServlet {

    @EJB
    private OrderService orderService;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute(AuthServlet.SESSION_USER_KEY);

        List<Order> myOrders = orderService.getOrdersByUser(user.getId());

        req.setAttribute("myOrders", myOrders);
        req.getRequestDispatcher("/WEB-INF/views/user/my-orders.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        String path = req.getPathInfo();
        if ("/cancel".equals(path)) {
            int orderId = parseIntOrDefault(req.getParameter("orderId"), 0);

            User user = (User) req.getSession().getAttribute(AuthServlet.SESSION_USER_KEY);
            orderService.getOrderById(orderId).ifPresent(order -> {
                if (user.getFullName().equals(order.getCustomerName())
                        && order.getStatus() != Order.Status.DELIVERED
                        && order.getStatus() != Order.Status.CANCELLED) {
                    orderService.cancelOrder(orderId);
                }
            });

            resp.sendRedirect(req.getContextPath() + "/my-orders?msg=cancelled");
            return;
        }

        resp.sendRedirect(req.getContextPath() + "/my-orders");
    }

    private int parseIntOrDefault(String v, int def) {
        try {
            return Integer.parseInt(v);
        } catch (Exception e) {
            return def;
        }
    }
}