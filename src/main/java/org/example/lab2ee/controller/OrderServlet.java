package org.example.lab2ee.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.example.lab2ee.model.MenuItem;
import org.example.lab2ee.model.Order;
import org.example.lab2ee.model.OrderItem;
import org.example.lab2ee.service.MenuService;
import org.example.lab2ee.service.OrderService;
import org.example.lab2ee.service.ServiceFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebServlet("/order/*")
public class OrderServlet extends HttpServlet {

    private static final String CART_KEY = "cart";
    private MenuService menuService;
    private OrderService orderService;

    @Override
    public void init() {
        menuService = ServiceFactory.getMenuService();
        orderService = ServiceFactory.getOrderService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String path = req.getPathInfo();
        if (path == null) path = "/";
        switch (path) {
            case "/cart":
                showCart(req, resp);
                break;
            case "/checkout":
                showCheckout(req, resp);
                break;
            case "/confirmation":
                showConfirmation(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/menu");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();
        if (path == null) path = "/";
        switch (path) {
            case "/add":
                addToCart(req, resp);
                break;
            case "/remove":
                removeFromCart(req, resp);
                break;
            case "/place":
                placeOrder(req, resp);
                break;
            default:
                resp.sendRedirect(req.getContextPath() + "/menu");
        }
    }

    private void showCart(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("cart", getCart(req.getSession()));
        req.getRequestDispatcher("/WEB-INF/views/user/cart.jsp").forward(req, resp);
    }

    private void showCheckout(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<OrderItem> cart = getCart(req.getSession());
        if (cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/order/cart");
            return;
        }
        req.setAttribute("cart", cart);
        req.getRequestDispatcher("/WEB-INF/views/user/checkout.jsp").forward(req, resp);
    }

    private void showConfirmation(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Integer orderId = (Integer) req.getSession().getAttribute("lastOrderId");
        if (orderId != null) orderService.getOrderById(orderId).ifPresent(o -> req.setAttribute("order", o));
        req.getRequestDispatcher("/WEB-INF/views/user/confirmation.jsp").forward(req, resp);
    }

    private void addToCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int menuItemId = parseIntOrDefault(req.getParameter("menuItemId"), 0);
        int quantity = parseIntOrDefault(req.getParameter("quantity"), 1);
        if (quantity < 1) quantity = 1;

        Optional<MenuItem> menuItem = menuService.getMenuItemById(menuItemId);
        if (menuItem.isEmpty() || !menuItem.get().isAvailable()) {
            resp.sendRedirect(req.getContextPath() + "/menu?error=item_unavailable");
            return;
        }
        List<OrderItem> cart = getCart(req.getSession());
        boolean found = false;
        for (OrderItem item : cart) {
            if (item.getMenuItem().getId() == menuItemId) {
                item.setQuantity(item.getQuantity() + quantity);
                found = true;
                break;
            }
        }
        if (!found) cart.add(new OrderItem(cart.size() + 1, menuItem.get(), quantity, ""));
        req.getSession().setAttribute(CART_KEY, cart);
        resp.sendRedirect(req.getContextPath() + "/order/cart");
    }

    private void removeFromCart(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int menuItemId = parseIntOrDefault(req.getParameter("menuItemId"), 0);
        List<OrderItem> cart = getCart(req.getSession());
        cart.removeIf(item -> item.getMenuItem().getId() == menuItemId);
        req.getSession().setAttribute(CART_KEY, cart);
        resp.sendRedirect(req.getContextPath() + "/order/cart");
    }

    private void placeOrder(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        List<OrderItem> cart = getCart(req.getSession());
        if (cart.isEmpty()) {
            resp.sendRedirect(req.getContextPath() + "/order/cart");
            return;
        }

        String name = sanitize(req.getParameter("customerName"));
        String phone = sanitize(req.getParameter("customerPhone"));
        String address = sanitize(req.getParameter("deliveryAddress"));
        String notes = sanitize(req.getParameter("notes"));

        if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
            req.setAttribute("cart", cart);
            req.setAttribute("error", "Будь ласка, заповніть усі обов'язкові поля.");
            req.getRequestDispatcher("/WEB-INF/views/user/checkout.jsp").forward(req, resp);
            return;
        }

        Order order = new Order();
        order.setCustomerName(name);
        order.setCustomerPhone(phone);
        order.setDeliveryAddress(address);
        order.setNotes(notes);
        order.setItems(new ArrayList<>(cart));

        Order saved = orderService.createOrder(order);
        req.getSession().removeAttribute(CART_KEY);
        req.getSession().setAttribute("lastOrderId", saved.getId());
        resp.sendRedirect(req.getContextPath() + "/order/confirmation");
    }

    @SuppressWarnings("unchecked")
    private List<OrderItem> getCart(HttpSession session) {
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute(CART_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_KEY, cart);
        }
        return cart;
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
        return input.trim()
                .replace("&", "&amp;").replace("<", "&lt;")
                .replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#x27;");
    }
}
