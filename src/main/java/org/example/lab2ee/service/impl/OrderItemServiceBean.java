package org.example.lab2ee.service.impl;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import org.example.lab2ee.dao.MenuItemDAO;
import org.example.lab2ee.dao.OrderItemDAO;
import org.example.lab2ee.model.MenuItem;
import org.example.lab2ee.model.Order;
import org.example.lab2ee.model.OrderItem;
import org.example.lab2ee.service.OrderItemService;

import java.util.List;

@Stateless
public class OrderItemServiceBean implements OrderItemService {

    @EJB private OrderItemDAO orderItemDAO;
    @EJB private MenuItemDAO  menuItemDAO;


    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void saveItems(Order order, List<OrderItem> items) {
        for (OrderItem item : items) {

            MenuItem menuItem = menuItemDAO.findById(item.getMenuItem().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Страва з ID " + item.getMenuItem().getId() + " не знайдена"));

            if (!menuItem.isAvailable()) {
                throw new IllegalStateException(
                        "Страва '" + menuItem.getName() + "' більше недоступна. " +
                        "Замовлення скасовано. Транзакція відкотиться — " +
                        "замовлення не збережеться.");
            }

            item.setOrder(order);
            item.setMenuItem(menuItem);
            orderItemDAO.save(item);
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void validateItems(List<OrderItem> items) {
        for (OrderItem item : items) {
            MenuItem menuItem = menuItemDAO.findById(item.getMenuItem().getId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Страва з ID " + item.getMenuItem().getId() + " не знайдена"));

            if (!menuItem.isAvailable()) {
                throw new IllegalStateException(
                        "Страва '" + menuItem.getName() + "' недоступна. " +
                                "Транзакція відкотиться — замовлення не збережеться.");
            }
        }
    }
}