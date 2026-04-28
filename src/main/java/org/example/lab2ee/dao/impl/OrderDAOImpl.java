package org.example.lab2ee.dao.impl;

import org.example.lab2ee.dao.OrderDAO;
import org.example.lab2ee.db.ConnectionPool;
import org.example.lab2ee.model.MenuItem;
import org.example.lab2ee.model.Order;
import org.example.lab2ee.model.OrderItem;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class OrderDAOImpl implements OrderDAO {

    private static final String SQL_FIND_ALL =
            "SELECT id, customer_name, customer_phone, delivery_address, " +
            "       notes, status, created_at, updated_at " +
            "FROM orders ORDER BY created_at DESC";

    private static final String SQL_FIND_BY_ID =
            "SELECT id, customer_name, customer_phone, delivery_address, " +
            "       notes, status, created_at, updated_at " +
            "FROM orders WHERE id = ?";

    private static final String SQL_FIND_BY_STATUS =
            "SELECT id, customer_name, customer_phone, delivery_address, " +
            "       notes, status, created_at, updated_at " +
            "FROM orders WHERE status = ? ORDER BY created_at DESC";


    private static final String SQL_FIND_BY_CUSTOMER =
            "SELECT id, customer_name, customer_phone, delivery_address, " +
            "       notes, status, created_at, updated_at " +
            "FROM orders WHERE customer_name ILIKE ? ORDER BY created_at DESC";

    private static final String SQL_FIND_ITEMS_BY_ORDER =
            "SELECT oi.id, oi.quantity, oi.special_instructions, " +
            "       mi.id AS mi_id, mi.name, mi.description, mi.price, " +
            "       mi.category, mi.available, mi.calories " +
            "FROM order_items oi " +
            "JOIN menu_items mi ON oi.menu_item_id = mi.id " +
            "WHERE oi.order_id = ? ORDER BY oi.id";

    private static final String SQL_INSERT_ORDER =
            "INSERT INTO orders (customer_name, customer_phone, delivery_address, notes, status, created_at, updated_at) " +
            "VALUES (?, ?, ?, ?, ?, NOW(), NOW())";

    private static final String SQL_INSERT_ITEM =
            "INSERT INTO order_items (order_id, menu_item_id, quantity, special_instructions) " +
            "VALUES (?, ?, ?, ?)";

    private static final String SQL_UPDATE_STATUS =
            "UPDATE orders SET status = ?, updated_at = NOW() WHERE id = ?";

    private static final String SQL_DELETE =
            "DELETE FROM orders WHERE id = ?";

    // ── CREATE ────────────────────────────────────────────────────────────────


    @Override
    public Order save(Order order) {
        Connection con = null;
        try {
            con = ConnectionPool.getConnection();
            con.setAutoCommit(false);

            int orderId = insertOrder(con, order);
            order.setId(orderId);

            for (OrderItem item : order.getItems()) {
                insertOrderItem(con, orderId, item);
            }

            con.commit();
            return order;

        } catch (SQLException e) {
            if (con != null) {
                try { con.rollback(); } catch (SQLException ex) { /* ignore rollback error */ }
            }
            throw new RuntimeException("Помилка при збереженні замовлення: " + e.getMessage(), e);
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true); // відновлюємо auto-commit
                    con.close();             // повертаємо в пул
                } catch (SQLException ex) { /* ignore */ }
            }
        }
    }

    private int insertOrder(Connection con, Order order) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(
                SQL_INSERT_ORDER, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, order.getCustomerName());
            stmt.setString(2, order.getCustomerPhone());
            stmt.setString(3, order.getDeliveryAddress());
            stmt.setString(4, order.getNotes());
            stmt.setString(5, order.getStatus().name());

            stmt.executeUpdate();

            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
                throw new SQLException("Не вдалось отримати згенерований id замовлення");
            }
        }
    }

    private void insertOrderItem(Connection con, int orderId, OrderItem item) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(SQL_INSERT_ITEM)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, item.getMenuItem().getId());
            stmt.setInt(3, item.getQuantity());
            stmt.setString(4, item.getSpecialInstructions() != null
                    ? item.getSpecialInstructions() : "");
            stmt.executeUpdate();
        }
    }

    // ── READ ──────────────────────────────────────────────────────────────────

    @Override
    public Optional<Order> findById(int id) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_FIND_BY_ID)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Order order = mapOrderRow(rs);
                    loadItems(con, order);
                    return Optional.of(order);
                }
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при пошуку замовлення id=" + id, e);
        }
    }

    @Override
    public List<Order> findAll() {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_FIND_ALL);
             ResultSet rs = stmt.executeQuery()) {

            List<Order> result = new ArrayList<>();
            while (rs.next()) {
                Order order = mapOrderRow(rs);
                loadItems(con, order);
                result.add(order);
            }
            return result;

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при отриманні всіх замовлень", e);
        }
    }

    @Override
    public List<Order> findByStatus(Order.Status status) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_FIND_BY_STATUS)) {

            stmt.setString(1, status.name()); // PreparedStatement — захист від ін'єкції
            try (ResultSet rs = stmt.executeQuery()) {
                List<Order> result = new ArrayList<>();
                while (rs.next()) {
                    Order order = mapOrderRow(rs);
                    loadItems(con, order);
                    result.add(order);
                }
                return result;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при пошуку замовлень за статусом: " + status, e);
        }
    }

    /**
     * Пошук за іменем клієнта.
     * ILIKE ? з параметром "%" + name + "%" — захист від SQL-ін'єкції.
     */
    @Override
    public List<Order> findByCustomerName(String name) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_FIND_BY_CUSTOMER)) {

            stmt.setString(1, "%" + name.trim() + "%");
            try (ResultSet rs = stmt.executeQuery()) {
                List<Order> result = new ArrayList<>();
                while (rs.next()) {
                    Order order = mapOrderRow(rs);
                    loadItems(con, order);
                    result.add(order);
                }
                return result;
            }

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при пошуку замовлень за ім'ям: " + name, e);
        }
    }

    // ── UPDATE ────────────────────────────────────────────────────────────────

    @Override
    public Order updateStatus(int orderId, Order.Status newStatus) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_UPDATE_STATUS)) {

            stmt.setString(1, newStatus.name());
            stmt.setInt(2, orderId);
            int affected = stmt.executeUpdate();

            if (affected == 0) {
                throw new RuntimeException("Замовлення з id=" + orderId + " не знайдено");
            }
            return findById(orderId).orElseThrow();

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при оновленні статусу замовлення id=" + orderId, e);
        }
    }

    // ── DELETE ────────────────────────────────────────────────────────────────

    @Override
    public boolean delete(int id) {
        try (Connection con = ConnectionPool.getConnection();
             PreparedStatement stmt = con.prepareStatement(SQL_DELETE)) {

            stmt.setInt(1, id);
            // Позиції order_items видаляються каскадно через FK ON DELETE CASCADE
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new RuntimeException("Помилка при видаленні замовлення id=" + id, e);
        }
    }

    // ── Маппінг ───────────────────────────────────────────────────────────────

    private Order mapOrderRow(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setCustomerName(rs.getString("customer_name"));
        order.setCustomerPhone(rs.getString("customer_phone"));
        order.setDeliveryAddress(rs.getString("delivery_address"));
        order.setNotes(rs.getString("notes"));
        order.setStatus(Order.Status.valueOf(rs.getString("status")));
        // Timestamp → LocalDateTime
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) order.setCreatedAt(createdAt.toLocalDateTime());
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) order.setUpdatedAt(updatedAt.toLocalDateTime());
        return order;
    }


    private void loadItems(Connection con, Order order) throws SQLException {
        try (PreparedStatement stmt = con.prepareStatement(SQL_FIND_ITEMS_BY_ORDER)) {
            stmt.setInt(1, order.getId());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MenuItem mi = new MenuItem();
                    mi.setId(rs.getInt("mi_id"));
                    mi.setName(rs.getString("name"));
                    mi.setDescription(rs.getString("description"));
                    mi.setPrice(rs.getBigDecimal("price"));
                    mi.setCategory(MenuItem.Category.valueOf(rs.getString("category")));
                    mi.setAvailable(rs.getBoolean("available"));
                    mi.setCalories(rs.getInt("calories"));

                    OrderItem item = new OrderItem();
                    item.setId(rs.getInt("id"));
                    item.setMenuItem(mi);
                    item.setQuantity(rs.getInt("quantity"));
                    item.setSpecialInstructions(rs.getString("special_instructions"));
                    order.addItem(item);
                }
            }
        }
    }
}
