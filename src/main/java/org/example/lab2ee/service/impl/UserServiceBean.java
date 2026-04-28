package org.example.lab2ee.service.impl;

import jakarta.ejb.Stateless;
import org.example.lab2ee.dao.UserDAO;
import org.example.lab2ee.dao.impl.UserDAOImpl;
import org.example.lab2ee.model.User;
import org.example.lab2ee.service.UserService;

import java.util.Optional;


@Stateless
public class UserServiceBean implements UserService {

    private final UserDAO dao = new UserDAOImpl();

    @Override
    public Optional<User> authenticate(String username, String password) {
        if (username == null || password == null) return Optional.empty();
        Optional<User> user = dao.findByUsername(username.trim());
        if (user.isEmpty()) return Optional.empty();
        // Порівнюємо password_hash з БД
        // ЛР 4: plain-text; у продакшні → BCrypt.checkpw(password, user.get().getPasswordHash())
        return password.equals(user.get().getPasswordHash())
                ? user : Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null) return Optional.empty();
        return dao.findByUsername(username.trim());
    }
}
