package org.example.lab2ee.service.impl;

import org.example.lab2ee.model.User;
import org.example.lab2ee.service.UserService;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

public class UserServiceStub implements UserService {


    private static final Map<String, User> USERS = new LinkedHashMap<>();

    private static final Map<String, String> PASSWORDS = new LinkedHashMap<>();

    static {
        register(1, "admin", "Адміністратор Системи", "admin@smakua.com", "+380501110000", User.Role.ADMIN, "admin123");
        register(2, "user", "Іван Іваненко", "ivan@example.com", "+380502220001", User.Role.USER, "user123");
        register(3, "vasyl", "Василь Петренко", "vasyl@example.com", "+380503330002", User.Role.USER, "pass123");
    }

    private static void register(int id, String username, String fullName,
                                 String email, String phone, User.Role role, String password) {
        User u = new User(id, username, fullName, email, phone, role);
        USERS.put(username, u);
        PASSWORDS.put(username, password);
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        User user = USERS.get(username.trim());
        if (user == null) return Optional.empty();
        String stored = PASSWORDS.get(username.trim());
        // In production: BCrypt.checkpw(password, storedHash)
        if (!password.equals(stored)) return Optional.empty();
        return Optional.of(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        return Optional.ofNullable(USERS.get(username.trim()));
    }
}
