package org.example.lab2ee.dao;

import jakarta.ejb.Local;
import org.example.lab2ee.model.OrderItem;

@Local
public interface OrderItemDAO {
    OrderItem save(OrderItem item);
}