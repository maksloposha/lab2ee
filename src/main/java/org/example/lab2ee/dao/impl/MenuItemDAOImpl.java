package org.example.lab2ee.dao.impl;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.lab2ee.dao.MenuItemDAO;
import org.example.lab2ee.model.MenuItem;

import java.util.List;
import java.util.Optional;


@Stateless
public class MenuItemDAOImpl implements MenuItemDAO {

    @PersistenceContext(unitName = "FoodOrderingPU")
    private EntityManager em;

    @Override
    public MenuItem save(MenuItem item) {
        em.persist(item);
        em.flush();
        return item;
    }

    @Override
    public Optional<MenuItem> findById(int id) {
        return Optional.ofNullable(em.find(MenuItem.class, id));
    }

    @Override
    public List<MenuItem> findAll() {
        return em.createQuery(
                        "SELECT m FROM MenuItem m ORDER BY m.category, m.name" ,
                        MenuItem.class)
                .getResultList();
    }

    @Override
    public List<MenuItem> findByCategory(MenuItem.Category category) {
        return em.createQuery(
                        "SELECT m FROM MenuItem m WHERE m.category = :cat ORDER BY m.name" ,
                        MenuItem.class)
                .setParameter("cat" , category) // параметр — захист від SQL-ін'єкції
                .getResultList();
    }

    @Override
    public List<MenuItem> findAvailable() {
        return em.createQuery(
                        "SELECT m FROM MenuItem m WHERE m.available = TRUE ORDER BY m.category, m.name" ,
                        MenuItem.class)
                .getResultList();
    }


    @Override
    public List<MenuItem> search(String keyword) {
        if (keyword == null || keyword.isBlank()) return findAll();
        String pattern = "%" + keyword.trim().toLowerCase() + "%";
        return em.createQuery(
                        "SELECT m FROM MenuItem m " +
                                "WHERE LOWER(m.name) LIKE :kw OR LOWER(m.description) LIKE :kw " +
                                "ORDER BY m.name" ,
                        MenuItem.class)
                .setParameter("kw" , pattern)
                .getResultList();
    }


    @Override
    public MenuItem update(MenuItem item) {
        return em.merge(item);
    }

    @Override
    public boolean delete(int id) {
        MenuItem item = em.find(MenuItem.class, id);
        if (item == null) return false;
        em.remove(item);
        return true;
    }
}