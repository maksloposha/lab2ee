package org.example.lab2ee.service;

import jakarta.ejb.Local;
import org.example.lab2ee.model.User;

import java.util.Optional;

@Local
public interface UserService {
    Optional<User> authenticate(String username, String password);

    Optional<User> findByUsername(String username);

    User register(String username, String password, String fullName,
                  String email, String phone);
}
