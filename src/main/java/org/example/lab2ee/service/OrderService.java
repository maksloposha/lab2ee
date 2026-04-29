package org.example.lab2ee.service;

import jakarta.ejb.Local;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import org.example.lab2ee.model.Order;
import org.example.lab2ee.model.User;

import java.util.List;
import java.util.Optional;

@Local
public interface OrderService {
    List<Order> getAllOrders();

    List<Order> getOrdersByStatus(Order.Status status);

    Optional<Order> getOrderById(int id);

    Order createOrder(Order order, User user);

    Order updateOrderStatus(int orderId, Order.Status newStatus);

    boolean cancelOrder(int orderId);

    Order validateOrderForReview(int orderId, int userId);

    List<Order> getOrdersByUser(int userId);
}
