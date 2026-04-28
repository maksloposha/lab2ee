package org.example.lab2ee.dao;

import org.example.lab2ee.model.User;

import java.util.Optional;


public interface UserDAO {
    Optional<User> findByUsername(String username);
    Optional<User> findById(int id);
    User           save(User user);
}
