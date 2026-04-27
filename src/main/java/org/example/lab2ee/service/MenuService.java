package org.example.lab2ee.service;

import jakarta.ejb.Local;
import org.example.lab2ee.model.MenuItem;

import java.util.List;
import java.util.Optional;

@Local
public interface MenuService {
    List<MenuItem> getAllMenuItems();

    List<MenuItem> getMenuItemsByCategory(MenuItem.Category category);

    List<MenuItem> getAvailableMenuItems();

    Optional<MenuItem> getMenuItemById(int id);

    MenuItem createMenuItem(MenuItem item);

    MenuItem updateMenuItem(MenuItem item);

    boolean deleteMenuItem(int id);

    List<MenuItem.Category> getAllCategories();
}
