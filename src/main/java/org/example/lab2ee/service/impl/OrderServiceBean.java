package org.example.lab2ee.service.impl;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import org.example.lab2ee.model.Order;
import org.example.lab2ee.model.OrderItem;
import org.example.lab2ee.service.MenuService;
import org.example.lab2ee.service.OrderService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


@Stateless
public class OrderServiceBean implements OrderService {

    @EJB
    private MenuService menuService;

    private static final Map<Integer, Order> store = new LinkedHashMap<>();
    private static final AtomicInteger       seq   = new AtomicInteger(5);

    static {
        MenuServiceBean ms = new MenuServiceBean();

        Order o1 = new Order(1, "Олена Коваленко", "+380501234567",
                "вул. Хрещатик, 1, кв. 5", Order.Status.PENDING,
                LocalDateTime.now().minusMinutes(10));
        addItem(o1, ms, 1, 2, "");
        addItem(o1, ms, 3, 1, "Без сметани");
        store.put(1, o1);

        Order o2 = new Order(2, "Михайло Петренко", "+380672345678",
                "вул. Велика Васильківська, 45", Order.Status.PREPARING,
                LocalDateTime.now().minusMinutes(35));
        addItem(o2, ms, 4, 1, "Medium прожарювання");
        addItem(o2, ms, 8, 1, "");
        store.put(2, o2);

        Order o3 = new Order(3, "Ірина Савченко", "+380933456789",
                "пр. Перемоги, 67", Order.Status.DELIVERED,
                LocalDateTime.now().minusHours(2));
        addItem(o3, ms, 5, 2, "");
        store.put(3, o3);

        Order o4 = new Order(4, "Дмитро Іваненко", "+380504567890",
                "вул. Антоновича, 23", Order.Status.CANCELLED,
                LocalDateTime.now().minusHours(1));
        addItem(o4, ms, 7, 1, "");
        o4.setNotes("Клієнт не відповідає");
        store.put(4, o4);

        Order o5 = new Order(5, "Василь Петренко", "+380503330002",
                "вул. Сагайдачного, 10", Order.Status.READY,
                LocalDateTime.now().minusMinutes(20));
        addItem(o5, ms, 2, 1, "");
        addItem(o5, ms, 9, 1, "");
        store.put(5, o5);
    }

    private static void addItem(Order o, MenuServiceBean ms, int menuId, int qty, String note) {
        ms.getMenuItemById(menuId).ifPresent(mi ->
                o.addItem(new OrderItem(o.getItems().size() + 1, mi, qty, note)));
    }


    @Override
    public List<Order> getAllOrders() {
        return new ArrayList<>(store.values());
    }

    @Override
    
    public List<Order> getOrdersByStatus(Order.Status status) {
        return store.values().stream()
                .filter(o -> o.getStatus() == status)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Order> getOrderById(int id) {
        return Optional.ofNullable(store.get(id));
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
                throw new IllegalArgumentException(
                        "Кількість страви має бути від 1 до 50");
            }
        }

        if (order.getCustomerName() == null || order.getCustomerName().isBlank()) {
            throw new IllegalArgumentException("Ім'я замовника є обов'язковим");
        }
        if (order.getDeliveryAddress() == null || order.getDeliveryAddress().isBlank()) {
            throw new IllegalArgumentException("Адреса доставки є обов'язковою");
        }

        int newId = seq.incrementAndGet();
        order.setId(newId);
        order.setStatus(Order.Status.PENDING);
        store.put(newId, order);
        return order;
    }

    @Override
    public Order updateOrderStatus(int orderId, Order.Status newStatus) {
        Order order = store.get(orderId);
        if (order == null) {
            throw new NoSuchElementException("Замовлення з ID " + orderId + " не знайдено");
        }
        // Бізнес-правило: термінальні статуси — незмінні
        if (order.getStatus() == Order.Status.DELIVERED) {
            throw new IllegalStateException("Неможливо змінити статус доставленого замовлення");
        }
        if (order.getStatus() == Order.Status.CANCELLED) {
            throw new IllegalStateException("Неможливо змінити статус скасованого замовлення");
        }
        order.setStatus(newStatus);
        return order;
    }

    @Override
    public boolean cancelOrder(int orderId) {
        Order order = store.get(orderId);
        if (order == null) return false;
        if (order.getStatus() == Order.Status.DELIVERED) {
            throw new IllegalStateException("Неможливо скасувати доставлене замовлення");
        }
        order.setStatus(Order.Status.CANCELLED);
        return true;
    }
}
