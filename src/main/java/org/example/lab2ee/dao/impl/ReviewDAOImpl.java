package org.example.lab2ee.dao.impl;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.example.lab2ee.dao.ReviewDAO;
import org.example.lab2ee.model.Review;

import java.util.List;
import java.util.Optional;

@Stateless
public class ReviewDAOImpl implements ReviewDAO {

    @PersistenceContext(unitName = "FoodOrderingPU")
    private EntityManager em;

    @Override
    public Review save(Review review) {
        em.persist(review);
        em.flush();
        return review;
    }

    @Override
    public Optional<Review> findById(int id) {
        return Optional.ofNullable(em.find(Review.class, id));
    }

    @Override
    public List<Review> findByOrderId(int orderId) {
        return em.createQuery(
                "SELECT r FROM Review r WHERE r.order.id = :orderId ORDER BY r.createdAt DESC",
                Review.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    @Override
    public List<Review> findByUserId(int userId) {
        return em.createQuery(
                "SELECT r FROM Review r WHERE r.user.id = :userId ORDER BY r.createdAt DESC",
                Review.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Review> findByMinRating(int minRating) {
        return em.createQuery(
                "SELECT r FROM Review r WHERE r.rating >= :min ORDER BY r.rating DESC",
                Review.class)
                .setParameter("min", minRating)
                .getResultList();
    }

    @Override
    public boolean existsByOrderAndUser(int orderId, int userId) {
        try {
            em.createQuery(
                    "SELECT r FROM Review r WHERE r.order.id = :orderId AND r.user.id = :userId",
                    Review.class)
                    .setParameter("orderId", orderId)
                    .setParameter("userId", userId)
                    .getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

    @Override
    public boolean delete(int id) {
        Review r = em.find(Review.class, id);
        if (r == null) return false;
        em.remove(r);
        return true;
    }
}