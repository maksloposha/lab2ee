package org.example.lab2ee.service.impl;

import org.example.lab2ee.model.User;
import org.example.lab2ee.service.UserService;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class UserServiceStub implements UserService {

    private static final Map<String, User>   USERS     = new LinkedHashMap<>();
    private static final Map<String, String> PASSWORDS = new LinkedHashMap<>();
    private static final AtomicInteger       SEQ       = new AtomicInteger(3);

    static {
        reg(1, "admin", "Адміністратор Системи", "admin@smakua.com", "+380501110000", User.Role.ADMIN, "admin123");
        reg(2, "user",  "Іван Іваненко",          "ivan@example.com", "+380502220001", User.Role.USER,  "user123");
        reg(3, "vasyl", "Василь Петренко",          "vasyl@example.com","+380503330002", User.Role.USER,  "pass123");
    }

    private static void reg(int id, String u, String name, String email,
                             String phone, User.Role role, String pw) {
        USERS.put(u, new User(id, u, name, email, phone, role));
        PASSWORDS.put(u, pw);
    }

    @Override
    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        User user = USERS.get(username.trim());
        if (user == null) return Optional.empty();
        return password.equals(PASSWORDS.get(username.trim()))
                ? Optional.of(user) : Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        return Optional.ofNullable(USERS.get(username.trim()));
    }

    @Override
    public User register(String username, String password,
                         String fullName, String email, String phone) {
        if (username == null || username.isBlank())
            throw new IllegalArgumentException("Логін є обов'язковим");
        if (username.length() < 3 || username.length() > 50)
            throw new IllegalArgumentException("Логін має бути від 3 до 50 символів");
        if (!username.matches("[a-zA-Z0-9_]+"))
            throw new IllegalArgumentException("Логін може містити лише літери, цифри та підкреслення");
        if (password == null || password.length() < 6)
            throw new IllegalArgumentException("Пароль має бути не менше 6 символів");
        if (fullName == null || fullName.isBlank())
            throw new IllegalArgumentException("Ім'я є обов'язковим");
        if (email == null || !email.contains("@"))
            throw new IllegalArgumentException("Невірний формат email");
        if (USERS.containsKey(username.trim()))
            throw new IllegalArgumentException("Користувач з логіном '" + username + "' вже існує");

        int id = SEQ.incrementAndGet();
        User user = new User(id, username.trim(), fullName.trim(),
                email.trim(), phone != null ? phone.trim() : "", User.Role.USER);
        USERS.put(username.trim(), user);
        PASSWORDS.put(username.trim(), password);
        return user;
    }
}
