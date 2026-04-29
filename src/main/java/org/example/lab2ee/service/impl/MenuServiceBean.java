package org.example.lab2ee.service.impl;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import org.example.lab2ee.dao.MenuItemDAO;
import org.example.lab2ee.model.MenuItem;
import org.example.lab2ee.service.MenuService;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Stateless
public class MenuServiceBean implements MenuService {

    @EJB
    private MenuItemDAO dao;

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<MenuItem> getAllMenuItems() {
        return dao.findAll();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<MenuItem> getMenuItemsByCategory(MenuItem.Category category) {
        return dao.findByCategory(category);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<MenuItem> getAvailableMenuItems() {
        return dao.findAvailable();
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Optional<MenuItem> getMenuItemById(int id) {
        return dao.findById(id);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<MenuItem.Category> getAllCategories() {
        return Arrays.asList(MenuItem.Category.values());
    }

    // ── Запис — REQUIRED ──────────────────────────────────────────────────────

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public MenuItem createMenuItem(MenuItem item) {
        if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Ціна має бути більше нуля");
        if (item.getName() == null || item.getName().isBlank())
            throw new IllegalArgumentException("Назва є обов'язковою");
        return dao.save(item);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public MenuItem updateMenuItem(MenuItem item) {
        dao.findById(item.getId()).orElseThrow(() ->
                new NoSuchElementException("Елемент меню з ID " + item.getId() + " не знайдено"));
        return dao.update(item);
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean deleteMenuItem(int id) {
        MenuItem item = dao.findById(id).orElse(null);
        if (item == null) return false;
        if (item.isAvailable())
            throw new IllegalStateException("Не можна видалити доступну страву. Спочатку деактивуйте.");
        return dao.delete(id);
    }

}