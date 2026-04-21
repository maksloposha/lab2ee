package org.example.lab2ee.service;

import org.example.lab2ee.model.User;

import java.util.Optional;


public interface UserService {
    Optional<User> authenticate(String username, String password);

    Optional<User> findByUsername(String username);
}
