package org.example.lab2ee.service;

import jakarta.ejb.Local;
import org.example.lab2ee.model.Order;

import java.util.List;
import java.util.Optional;

@Local
public interface OrderService {
    List<Order> getAllOrders();

    List<Order> getOrdersByStatus(Order.Status status);

    Optional<Order> getOrderById(int id);

    Order createOrder(Order order);

    Order updateOrderStatus(int orderId, Order.Status newStatus);

    boolean cancelOrder(int orderId);
}
