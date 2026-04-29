package org.example.lab2ee.service.impl;

import org.example.lab2ee.model.Order;
import org.example.lab2ee.model.OrderItem;
import org.example.lab2ee.service.OrderService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class OrderServiceStub implements OrderService {

    private static final Map<Integer, Order> orders = new LinkedHashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(5);

    static {
        MenuServiceStub ms = new MenuServiceStub();

        Order o1 = new Order(1, "Олена Коваленко", "+380501234567", "вул. Хрещатик, 1, кв. 5", Order.Status.PENDING, LocalDateTime.now().minusMinutes(10));
        addItem(o1, ms, 1, 2, "");
        addItem(o1, ms, 3, 1, "Без сметани");
        addItem(o1, ms, 12, 2, "");
        orders.put(1, o1);

        Order o2 = new Order(2, "Михайло Петренко", "+380672345678", "вул. Велика Васильківська, 45, кв. 12", Order.Status.PREPARING, LocalDateTime.now().minusMinutes(35));
        addItem(o2, ms, 4, 1, "Medium прожарювання");
        addItem(o2, ms, 2, 1, "");
        addItem(o2, ms, 8, 1, "");
        addItem(o2, ms, 10, 2, "");
        orders.put(2, o2);

        Order o3 = new Order(3, "Ірина Савченко", "+380933456789", "пр. Перемоги, 67, кв. 3", Order.Status.READY, LocalDateTime.now().minusMinutes(55));
        addItem(o3, ms, 5, 2, "");
        addItem(o3, ms, 9, 1, "");
        orders.put(3, o3);

        Order o4 = new Order(4, "Дмитро Іваненко", "+380504567890", "вул. Антоновича, 23, кв. 8", Order.Status.DELIVERED, LocalDateTime.now().minusHours(2));
        addItem(o4, ms, 7, 1, "");
        addItem(o4, ms, 11, 1, "Без цукру");
        orders.put(4, o4);

        Order o5 = new Order(5, "Тетяна Мороз", "+380675678901", "вул. Сагайдачного, 10, кв. 2", Order.Status.CANCELLED, LocalDateTime.now().minusHours(1));
        addItem(o5, ms, 4, 2, "");
        o5.setNotes("Клієнт не відповідає");
        orders.put(5, o5);
    }

    private static void addItem(Order o, MenuServiceStub ms, int menuId, int qty, String note) {
        ms.getMenuItemById(menuId).ifPresent(mi ->
                o.addItem(new OrderItem(o.getItems().size() + 1, mi, qty, note))
        );
    }

    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public List<Order> getOrdersByStatus(Order.Status status) {
        return orders.values().stream().filter(o -> o.getStatus() == status).collect(Collectors.toList());
    }

    @Override
    public Optional<Order> getOrderById(int id) {
        return Optional.ofNullable(orders.get(id));
    }

    @Override
    public Order createOrder(Order order) {
        int newId = idCounter.incrementAndGet();
        order.setId(newId);
        orders.put(newId, order);
        return order;
    }

    @Override
    public Order updateOrderStatus(int orderId, Order.Status newStatus) {
        Order order = orders.get(orderId);
        if (order == null) throw new NoSuchElementException("Order not found: " + orderId);
        order.setStatus(newStatus);
        return order;
    }

    @Override
    public boolean cancelOrder(int orderId) {
        Order order = orders.get(orderId);
        if (order == null) return false;
        order.setStatus(Order.Status.CANCELLED);
        return true;
    }

    @Override
    public Order validateOrderForReview(int orderId, int userId) {
        return null;
    }
}
