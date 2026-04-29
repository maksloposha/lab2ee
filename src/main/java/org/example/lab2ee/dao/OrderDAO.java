package org.example.lab2ee.dao;

import org.example.lab2ee.model.Order;

import java.util.List;
import java.util.Optional;


public interface OrderDAO {


    Order save(Order order);

    Optional<Order> findById(int id);
    List<Order>     findAll();


    List<Order>     findByStatus(Order.Status status);


    List<Order>     findByCustomerName(String name);

    Order updateStatus(int orderId, Order.Status newStatus);

    boolean delete(int id);

    Order saveOrderOnly(Order order);
}
