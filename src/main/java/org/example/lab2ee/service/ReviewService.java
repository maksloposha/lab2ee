package org.example.lab2ee.service;

import jakarta.ejb.Local;
import org.example.lab2ee.model.Review;
import org.example.lab2ee.model.User;

import java.util.List;

@Local
public interface ReviewService {
    Review addReview(int orderId, User currentUser, int rating, String comment);

    List<Review> getReviewsByOrder(int orderId);
    List<Review> getReviewsByUser(int userId);
    List<Review> getReviewsByMinRating(int minRating);
    boolean      existsByOrderAndUser(int orderId, int userId);
    boolean      deleteReview(int reviewId, User currentUser);
}