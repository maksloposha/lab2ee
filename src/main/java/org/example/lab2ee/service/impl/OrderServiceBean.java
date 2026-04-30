package org.example.lab2ee.service.impl;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import org.example.lab2ee.dao.OrderDAO;
import org.example.lab2ee.model.Order;
import org.example.lab2ee.model.User;
import org.example.lab2ee.service.OrderItemService;
import org.example.lab2ee.service.OrderService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.logging.Logger;


@Stateless
public class OrderServiceBean implements OrderService {
    private static final Logger log = Logger.getLogger(OrderServiceBean.class.getName());

    @EJB private OrderDAO         orderDAO;
    @EJB private OrderItemService orderItemService;

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Order> getOrdersByStatus(Order.Status status) {
        return orderDAO.findByStatus(status);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Optional<Order> getOrderById(int id) {
        return orderDAO.findById(id);
    }


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Order createOrder(Order order, User user) {
        // Базова валідація
        if (order.getItems() == null || order.getItems().isEmpty())
            throw new IllegalArgumentException(
                    "Замовлення має містити хоча б одну позицію");
        if (order.getCustomerName() == null || order.getCustomerName().isBlank())
            throw new IllegalArgumentException("Ім'я замовника є обов'язковим");
        if (order.getDeliveryAddress() == null || order.getDeliveryAddress().isBlank())
            throw new IllegalArgumentException("Адреса доставки є обов'язковою");

        order.setStatus(Order.Status.PENDING);
        order.setUser(user);

        Order orderSaved = orderDAO.saveOrderOnly(order);
        orderItemService.validateItems(orderSaved.getItems());
        return orderSaved;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Order updateOrderStatus(int orderId, Order.Status newStatus) {
        Order order = orderDAO.findById(orderId).orElseThrow(() ->
                new NoSuchElementException("Замовлення " + orderId + " не знайдено"));
        if (order.getStatus() == Order.Status.DELIVERED)
            throw new IllegalStateException(
                    "Неможливо змінити статус доставленого замовлення");
        if (order.getStatus() == Order.Status.CANCELLED)
            throw new IllegalStateException(
                    "Неможливо змінити статус скасованого замовлення");
        return orderDAO.updateStatus(orderId, newStatus);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean cancelOrder(int orderId) {
        Order order = orderDAO.findById(orderId).orElse(null);
        if (order == null) return false;
        if (order.getStatus() == Order.Status.DELIVERED)
            throw new IllegalStateException(
                    "Неможливо скасувати доставлене замовлення");
        orderDAO.updateStatus(orderId, Order.Status.CANCELLED);
        return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public Order validateOrderForReview(int orderId, int userId) {
        Order order = orderDAO.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Замовлення #" + orderId + " не знайдено"));
        if (order.getStatus() != Order.Status.DELIVERED)
            throw new IllegalStateException(
                    "Відгук можна залишити лише на доставлене замовлення. " +
                            "Поточний статус: " + order.getStatus().getDisplayName());
        return order;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Order> getOrdersByUser(int userId) {
        return orderDAO.findByUserId(userId);
    }
}