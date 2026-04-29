package org.example.lab2ee.dao;

import jakarta.ejb.Local;
import org.example.lab2ee.model.Review;

import java.util.List;
import java.util.Optional;

@Local
public interface ReviewDAO {
    Review          save(Review review);
    Optional<Review> findById(int id);
    List<Review>    findByOrderId(int orderId);
    List<Review>    findByUserId(int userId);

    List<Review>    findByMinRating(int minRating);

    boolean         existsByOrderAndUser(int orderId, int userId);
    boolean         delete(int id);
}