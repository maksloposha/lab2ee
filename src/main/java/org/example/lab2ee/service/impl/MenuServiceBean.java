package org.example.lab2ee.service.impl;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
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

    @EJB                              // ← було: new MenuItemDAOImpl()
    private MenuItemDAO dao;

    @Override
    public List<MenuItem> getAllMenuItems() {
        return dao.findAll();
    }

    @Override
    public List<MenuItem> getMenuItemsByCategory(MenuItem.Category category) {
        return dao.findByCategory(category);
    }

    @Override
    public List<MenuItem> getAvailableMenuItems() {
        return dao.findAvailable();
    }

    @Override
    public Optional<MenuItem> getMenuItemById(int id) {
        return dao.findById(id);
    }

    public List<MenuItem> search(String keyword) {
        return dao.search(keyword);
    }

    @Override
    public MenuItem createMenuItem(MenuItem item) {
        if (item.getPrice() == null || item.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Ціна елемента меню має бути більше нуля");
        }
        if (item.getName() == null || item.getName().isBlank()) {
            throw new IllegalArgumentException("Назва елемента меню є обов'язковою");
        }
        return dao.save(item);
    }

    @Override
    public MenuItem updateMenuItem(MenuItem item) {
        dao.findById(item.getId()).orElseThrow(() ->
                new NoSuchElementException("Елемент меню з ID " + item.getId() + " не знайдено"));
        return dao.update(item);
    }

    @Override
    public boolean deleteMenuItem(int id) {
        MenuItem item = dao.findById(id).orElse(null);
        if (item == null) return false;
        if (item.isAvailable()) {
            throw new IllegalStateException(
                    "Не можна видалити доступний елемент меню. Спочатку деактивуйте його.");
        }
        return dao.delete(id);
    }

    @Override
    public List<MenuItem.Category> getAllCategories() {
        return Arrays.asList(MenuItem.Category.values());
    }
}
