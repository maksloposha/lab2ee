package org.example.lab2ee.service.impl;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.example.lab2ee.dao.OrderDAO;
import org.example.lab2ee.model.Order;
import org.example.lab2ee.model.OrderItem;
import org.example.lab2ee.service.MenuService;
import org.example.lab2ee.service.OrderService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;


@Stateless
public class OrderServiceBean implements OrderService {

    @EJB
    private OrderDAO dao;

    @EJB
    private MenuService menuService;

    @Override
    public List<Order> getAllOrders() {
        return dao.findAll();
    }

    @Override
    public List<Order> getOrdersByStatus(Order.Status status) {
        return dao.findByStatus(status);
    }

    public List<Order> findByCustomerName(String name) {
        return dao.findByCustomerName(name);
    }

    @Override
    public Optional<Order> getOrderById(int id) {
        return dao.findById(id);
    }

    @Override
    public Order createOrder(Order order) {
        if (order.getItems() == null || order.getItems().isEmpty()) {
            throw new IllegalArgumentException("Замовлення має містити хоча б одну позицію");
        }
        for (OrderItem item : order.getItems()) {
            menuService.getMenuItemById(item.getMenuItem().getId())
                    .orElseThrow(() -> new NoSuchElementException(
                            "Страва з ID " + item.getMenuItem().getId() + " не знайдена"));
            if (!item.getMenuItem().isAvailable()) {
                throw new IllegalStateException(
                        "Страва '" + item.getMenuItem().getName() + "' недоступна для замовлення");
            }
            if (item.getQuantity() < 1 || item.getQuantity() > 50) {
                throw new IllegalArgumentException("Кількість страви має бути від 1 до 50");
            }
        }
        if (order.getCustomerName() == null || order.getCustomerName().isBlank()) {
            throw new IllegalArgumentException("Ім'я замовника є обов'язковим");
        }
        if (order.getDeliveryAddress() == null || order.getDeliveryAddress().isBlank()) {
            throw new IllegalArgumentException("Адреса доставки є обов'язковою");
        }

        order.setStatus(Order.Status.PENDING);
        return dao.save(order);
    }

    @Override
    public Order updateOrderStatus(int orderId, Order.Status newStatus) {
        Order order = dao.findById(orderId).orElseThrow(() ->
                new NoSuchElementException("Замовлення з ID " + orderId + " не знайдено"));

        if (order.getStatus() == Order.Status.DELIVERED) {
            throw new IllegalStateException("Неможливо змінити статус доставленого замовлення");
        }
        if (order.getStatus() == Order.Status.CANCELLED) {
            throw new IllegalStateException("Неможливо змінити статус скасованого замовлення");
        }
        return dao.updateStatus(orderId, newStatus);
    }

    @Override
    public boolean cancelOrder(int orderId) {
        Order order = dao.findById(orderId).orElse(null);
        if (order == null) return false;
        if (order.getStatus() == Order.Status.DELIVERED) {
            throw new IllegalStateException("Неможливо скасувати доставлене замовлення");
        }
        dao.updateStatus(orderId, Order.Status.CANCELLED);
        return true;
    }
}
