package org.example.lab2ee.service.impl;

import jakarta.ejb.Stateless;
import org.example.lab2ee.model.User;
import org.example.lab2ee.service.UserService;

import java.util.*;


@Stateless
public class UserServiceBean implements UserService {

    private static final Map<String, User>   USERS     = new LinkedHashMap<>();
    private static final Map<String, String> PASSWORDS = new LinkedHashMap<>();

    static {
        reg(1, "admin", "Адміністратор Системи", "admin@smakua.com", "+380501110000", User.Role.ADMIN,  "admin123");
        reg(2, "user",  "Іван Іваненко",          "ivan@example.com", "+380502220001", User.Role.USER,   "user123");
        reg(3, "vasyl", "Василь Петренко",          "vasyl@example.com","+380503330002", User.Role.USER,   "pass123");
    }

    private static void reg(int id, String u, String name, String email,
                             String phone, User.Role role, String pw) {
        USERS.put(u, new User(id, u, name, email, phone, role));
        PASSWORDS.put(u, pw);
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        User u = USERS.get(username.trim());
        if (u == null) return Optional.empty();
        return password.equals(PASSWORDS.get(username.trim()))
                ? Optional.of(u) : Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        return Optional.ofNullable(USERS.get(username.trim()));
    }
}
