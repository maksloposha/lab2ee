package org.example.lab2ee.dao;

import org.example.lab2ee.model.MenuItem;

import java.util.List;
import java.util.Optional;


public interface MenuItemDAO {


    MenuItem save(MenuItem item);

    Optional<MenuItem>  findById(int id);
    List<MenuItem>      findAll();
    List<MenuItem>      findByCategory(MenuItem.Category category);
    List<MenuItem>      findAvailable();


    List<MenuItem> search(String keyword);

    MenuItem update(MenuItem item);

    boolean delete(int id);
}
