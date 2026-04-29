package org.example.lab2ee.service.impl;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import org.example.lab2ee.dao.MenuItemDAO;
import org.example.lab2ee.dao.ReviewDAO;
import org.example.lab2ee.model.*;
import org.example.lab2ee.service.OrderService;
import org.example.lab2ee.service.ReviewService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;


@Stateless
public class ReviewServiceBean implements ReviewService {

    @EJB private ReviewDAO   reviewDAO;
    @EJB private MenuItemDAO menuItemDAO;


    @EJB private OrderService orderService;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Review addReview(int orderId, User currentUser, int rating, String comment) {

        if (rating < 1 || rating > 5)
            throw new IllegalArgumentException("Рейтинг має бути від 1 до 5");

        if (reviewDAO.existsByOrderAndUser(orderId, currentUser.getId()))
            throw new IllegalStateException("Ви вже залишили відгук на це замовлення");

        Order order = orderService.validateOrderForReview(orderId, currentUser.getId());

        Review review = new Review(order, currentUser, rating, comment);
        reviewDAO.save(review);

        for (OrderItem item : order.getItems()) {
            menuItemDAO.findById(item.getMenuItem().getId()).ifPresent(menuItem -> {
                int oldCount = menuItem.getRatingCount();
                BigDecimal oldRating = menuItem.getRating();
                int newCount = oldCount + 1;
                BigDecimal newRating = oldRating
                        .multiply(BigDecimal.valueOf(oldCount))
                        .add(BigDecimal.valueOf(rating))
                        .divide(BigDecimal.valueOf(newCount), 2, RoundingMode.HALF_UP);

                menuItem.setRating(newRating);
                menuItem.setRatingCount(newCount);
                menuItemDAO.update(menuItem);
            });
        }

        return review;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Review> getReviewsByOrder(int orderId) {
        return reviewDAO.findByOrderId(orderId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Review> getReviewsByUser(int userId) {
        return reviewDAO.findByUserId(userId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Review> getReviewsByMinRating(int minRating) {
        return reviewDAO.findByMinRating(minRating);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public boolean existsByOrderAndUser(int orderId, int userId) {
        return reviewDAO.existsByOrderAndUser(orderId, userId);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean deleteReview(int reviewId, User currentUser) {
        Review review = reviewDAO.findById(reviewId).orElse(null);
        if (review == null) return false;
        if (!currentUser.isAdmin() && review.getUser().getId() != currentUser.getId())
            throw new IllegalStateException("Не можна видалити чужий відгук");
        return reviewDAO.delete(reviewId);
    }
}