package org.example.lab2ee.dao.impl;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.lab2ee.dao.OrderItemDAO;
import org.example.lab2ee.model.OrderItem;

@Stateless
public class OrderItemDAOImpl implements OrderItemDAO {

    @PersistenceContext(unitName = "FoodOrderingPU")
    private EntityManager em;

    @Override
    public OrderItem save(OrderItem item) {
        em.persist(item);
        em.flush();
        return item;
    }
}