package org.example.lab2ee.service.impl;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.example.lab2ee.dao.UserDAO;
import org.example.lab2ee.model.User;
import org.example.lab2ee.service.UserService;

import java.util.Optional;

@Stateless
public class UserServiceBean implements UserService {

    @EJB
    private UserDAO dao;

    @Override
    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        Optional<User> user = dao.findByUsername(username.trim());
        if (user.isEmpty()) return Optional.empty();
        return password.equals(user.get().getPasswordHash())
                ? user : Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        return dao.findByUsername(username.trim());
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

        if (dao.findByUsername(username.trim()).isPresent())
            throw new IllegalArgumentException("Користувач з логіном '" + username + "' вже існує");

        User user = new User();
        user.setUsername(username.trim());
        user.setPasswordHash(password);
        user.setFullName(fullName.trim());
        user.setEmail(email.trim());
        user.setPhone(phone != null ? phone.trim() : "");
        user.setRole(User.Role.USER);

        return dao.save(user);
    }
}
