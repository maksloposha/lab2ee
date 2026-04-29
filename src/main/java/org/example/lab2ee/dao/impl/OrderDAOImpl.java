package org.example.lab2ee.dao.impl;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.lab2ee.dao.OrderDAO;
import org.example.lab2ee.model.Order;

import java.util.List;
import java.util.Optional;


@Stateless
public class OrderDAOImpl implements OrderDAO {

    @PersistenceContext(unitName = "FoodOrderingPU")
    private EntityManager em;


    @Override
    public Order save(Order order) {
        em.persist(order);
        em.flush();
        return order;
    }


    @Override
    public Optional<Order> findById(int id) {
        return Optional.ofNullable(em.find(Order.class, id));
    }

    @Override
    public List<Order> findAll() {
        return em.createQuery(
                        "SELECT o FROM Order o ORDER BY o.createdAt DESC" ,
                        Order.class)
                .getResultList();
    }


    @Override
    public List<Order> findByStatus(Order.Status status) {
        return em.createQuery(
                        "SELECT o FROM Order o WHERE o.status = :status ORDER BY o.createdAt DESC" ,
                        Order.class)
                .setParameter("status" , status)
                .getResultList();
    }


    @Override
    public List<Order> findByCustomerName(String name) {
        return em.createQuery(
                        "SELECT o FROM Order o WHERE LOWER(o.customerName) LIKE :name ORDER BY o.createdAt DESC" ,
                        Order.class)
                .setParameter("name" , "%" + name.toLowerCase() + "%")
                .getResultList();
    }


    @Override
    public Order updateStatus(int orderId, Order.Status newStatus) {
        Order order = em.find(Order.class, orderId);
        if (order == null) throw new RuntimeException("Замовлення " + orderId + " не знайдено");
        order.setStatus(newStatus);
        return em.merge(order);
    }


    @Override
    public boolean delete(int id) {
        Order order = em.find(Order.class, id);
        if (order == null) return false;
        em.remove(order);
        return true;
    }
}