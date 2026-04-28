package org.example.lab2ee.dao.impl;

import org.example.lab2ee.dao.MenuItemDAO;
import org.example.lab2ee.db.ConnectionPool;
import org.example.lab2ee.model.MenuItem;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuItemDAOImpl implements MenuItemDAO {

    // ── SQL константи ─────────────────────────────────────────────────────────

    private static final String SQL_FIND_ALL =
            "SELECT id, name, description, price, category, available, calories " +
            "FROM menu_items ORDER BY category, name";

    private static final String SQL_FIND_BY_ID =
            "SELECT id, name, description, price, category, available, calories " +
            "FROM menu_items WHERE id = ?";

    private static final String SQL_FIND_BY_CATEGORY =
            "SELECT id, name, description, price, category, available, calories " +
            "FROM menu_items WHERE category = ? ORDER BY name";

    private static final String SQL_FIND_AVAILABLE =
            "SELECT id, name, description, price, category, available, calories " +
            "FROM menu_items WHERE available = TRUE ORDER BY category, name";

    private static final String SQL_SEARCH =
            "SELECT id, name, description, price, category, available, calories " +
            "FROM menu_items " +
            "WHERE name ILIKE ? OR description ILIKE ? " +
            "ORDER BY name";

    private static final String SQL_INSERT =
            "INSERT INTO menu_items (name, description, price, category, available, calories) " +
            "VALUES (?, ?, ?, ?, ?, ?) ";

    private static final String SQL_UPDATE =
            "UPDATE menu_items " +
            "SET name = ?, description = ?, price = ?, category = ?, available = ?, calories = ? " +
            "WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM menu_items WHERE id = ?";


    @Override
    public MenuItem save(MenuItem item) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(
                     SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setBigDecimal(3, item.getPrice());
            stmt.setString(4, item.getCategory().name());
            stmt.setBoolean(5, item.isAvailable());
            stmt.setInt(6, item.getCalories());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Помилка збереження елемента меню: жодного рядка не вставлено");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    item.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Не вдалось отримати згенерований id");
                }
            }

            return item;

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при збереженні елемента меню: " + e.getMessage(), e);
        }
    }


    @Override
    public Optional<MenuItem> findById(int id) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_FIND_BY_ID)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при пошуку елемента меню за id=" + id, e);
        }
    }

    @Override
    public List<MenuItem> findAll() {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            List<MenuItem> result = new ArrayList<>();
            while (rs.next()) result.add(mapRow(rs));
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при отриманні всіх елементів меню", e);
        }
    }

    @Override
    public List<MenuItem> findByCategory(MenuItem.Category category) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_FIND_BY_CATEGORY)) {

            stmt.setString(1, category.name());

            try (ResultSet rs = stmt.executeQuery()) {
                List<MenuItem> result = new ArrayList<>();
                while (rs.next()) result.add(mapRow(rs));
                return result;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при пошуку за категорією: " + category, e);
        }
    }

    @Override
    public List<MenuItem> findAvailable() {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_FIND_AVAILABLE);
             ResultSet rs = stmt.executeQuery()) {

            List<MenuItem> result = new ArrayList<>();
            while (rs.next()) result.add(mapRow(rs));
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при отриманні доступних елементів меню", e);
        }
    }


    @Override
    public List<MenuItem> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();

        String pattern = "%" + keyword.trim() + "%";

        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_SEARCH)) {

            stmt.setString(1, pattern);
            stmt.setString(2, pattern);

            try (ResultSet rs = stmt.executeQuery()) {
                List<MenuItem> result = new ArrayList<>();
                while (rs.next()) result.add(mapRow(rs));
                return result;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при пошуку елементів меню за keyword=" + keyword, e);
        }
    }


    @Override
    public MenuItem update(MenuItem item) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_UPDATE)) {

            stmt.setString(1, item.getName());
            stmt.setString(2, item.getDescription());
            stmt.setBigDecimal(3, item.getPrice());
            stmt.setString(4, item.getCategory().name());
            stmt.setBoolean(5, item.isAvailable());
            stmt.setInt(6, item.getCalories());
            stmt.setInt(7, item.getId());

            int affected = stmt.executeUpdate();
            if (affected == 0) {
                throw new RuntimeException("Елемент меню з id=" + item.getId() + " не знайдено");
            }
            return item;

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при оновленні елемента меню id=" + item.getId(), e);
        }
    }


    @Override
    public boolean delete(int id) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_DELETE)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при видаленні елемента меню id=" + id, e);
        }
    }


    private MenuItem mapRow(ResultSet rs) throws SQLException {
        MenuItem item = new MenuItem();
        item.setId(rs.getInt("id"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setAvailable(rs.getBoolean("available"));
        item.setCalories(rs.getInt("calories"));
        item.setCategory(MenuItem.Category.valueOf(rs.getString("category")));
        return item;
    }
}
