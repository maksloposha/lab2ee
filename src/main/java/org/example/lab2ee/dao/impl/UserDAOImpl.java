package org.example.lab2ee.dao.impl;

import org.example.lab2ee.dao.UserDAO;
import org.example.lab2ee.db.ConnectionPool;
import org.example.lab2ee.model.User;

import java.sql.*;
import java.util.Optional;


public class UserDAOImpl implements UserDAO {

    private static final String SQL_FIND_BY_USERNAME =
            "SELECT id, username, password_hash, full_name, email, phone, role " +
            "FROM users WHERE username = ?";

    private static final String SQL_FIND_BY_ID =
            "SELECT id, username, password_hash, full_name, email, phone, role " +
            "FROM users WHERE id = ?";

    private static final String SQL_INSERT =
            "INSERT INTO users (username, password_hash, full_name, email, phone, role) " +
            "VALUES (?, ?, ?, ?, ?, ?)";

    @Override
    public Optional<User> findByUsername(String username) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_FIND_BY_USERNAME)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при пошуку користувача: " + username, e);
        }
    }

    @Override
    public Optional<User> findById(int id) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_FIND_BY_ID)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при пошуку користувача id=" + id, e);
        }
    }

    @Override
    public User save(User user) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getFullName());
            stmt.setString(4, user.getEmail());
            stmt.setString(5, user.getPhone());
            stmt.setString(6, user.getRole().name());
            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) user.setId(keys.getInt(1));
            }
            return user;

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при збереженні користувача: " + e.getMessage(), e);
        }
    }

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setUsername(rs.getString("username"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setRole(User.Role.valueOf(rs.getString("role")));
        return u;
    }
}
