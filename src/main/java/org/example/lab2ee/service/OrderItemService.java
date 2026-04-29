package org.example.lab2ee.service;

import jakarta.ejb.Local;
import org.example.lab2ee.model.Order;
import org.example.lab2ee.model.OrderItem;

import java.util.List;

@Local
public interface OrderItemService {

    void saveItems(Order order, List<OrderItem> items);
}