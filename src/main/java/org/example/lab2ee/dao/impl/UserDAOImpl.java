package org.example.lab2ee.dao.impl;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.example.lab2ee.dao.UserDAO;
import org.example.lab2ee.model.User;

import java.util.Optional;

@Stateless
public class UserDAOImpl implements UserDAO {

    @PersistenceContext(unitName = "FoodOrderingPU")
    private EntityManager em;

    @Override
    public Optional<User> findByUsername(String username) {
        try {
            User user = em.createQuery(
                            "SELECT u FROM User u WHERE u.username = :username" ,
                            User.class)
                    .setParameter("username" , username)
                    .getSingleResult();
            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public User save(User user) {
        em.persist(user);
        em.flush();
        return user;
    }
}