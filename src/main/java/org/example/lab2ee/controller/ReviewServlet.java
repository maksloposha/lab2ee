package org.example.lab2ee.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.example.lab2ee.model.Order;
import org.example.lab2ee.model.Review;
import org.example.lab2ee.model.User;
import org.example.lab2ee.service.OrderService;
import org.example.lab2ee.service.ReviewService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@WebServlet("/reviews/*")
public class ReviewServlet extends HttpServlet {

    @EJB private ReviewService reviewService;
    @EJB private OrderService  orderService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getPathInfo();
        if (path == null) path = "/";

        switch (path) {
            case "/my": {
                User user = currentUser(req);
                req.setAttribute("reviews", reviewService.getReviewsByUser(user.getId()));
                req.setAttribute("pageTitle", "Мої відгуки");
                req.getRequestDispatcher("/WEB-INF/views/user/my-reviews.jsp")
                   .forward(req, resp);
                break;
            }
            default: {
                // /reviews?orderId=5 — відгуки до конкретного замовлення
                int orderId = parseIntOrZero(req.getParameter("orderId"));
                Optional<Order> order = orderService.getOrderById(orderId);
                if (order.isEmpty()) {
                    resp.sendRedirect(req.getContextPath() + "/my-orders");
                    return;
                }
                req.setAttribute("order",   order.get());
                req.setAttribute("reviews", reviewService.getReviewsByOrder(orderId));
                req.setAttribute("alreadyReviewed",
                        reviewService.existsByOrderAndUser(orderId, currentUser(req).getId()));
                req.getRequestDispatcher("/WEB-INF/views/user/order-reviews.jsp")
                   .forward(req, resp);
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        req.setCharacterEncoding("UTF-8");
        String path = req.getPathInfo();
        if (path == null) path = "/";

        switch (path) {
            case "/add": {
                int    orderId = parseIntOrZero(req.getParameter("orderId"));
                int    rating  = parseIntOrZero(req.getParameter("rating"));
                String comment = req.getParameter("comment");
                User   user    = currentUser(req);

                try {
                    reviewService.addReview(orderId, user, rating, comment);
                    resp.sendRedirect(req.getContextPath() +
                            "/reviews?orderId=" + orderId + "&msg=added");
                } catch (IllegalArgumentException | IllegalStateException e) {
                    resp.sendRedirect(req.getContextPath() +
                            "/reviews?orderId=" + orderId + "&error=" +
                            java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
                }
                break;
            }
            case "/delete": {
                int  reviewId = parseIntOrZero(req.getParameter("id"));
                User user     = currentUser(req);
                reviewService.deleteReview(reviewId, user);
                resp.sendRedirect(req.getContextPath() + "/reviews/my");
                break;
            }
            default:
                resp.sendRedirect(req.getContextPath() + "/");
        }
    }

    private User currentUser(HttpServletRequest req) {
        return (User) req.getSession().getAttribute(AuthServlet.SESSION_USER_KEY);
    }

    private int parseIntOrZero(String v) {
        try { return Integer.parseInt(v); } catch (Exception e) { return 0; }
    }
}