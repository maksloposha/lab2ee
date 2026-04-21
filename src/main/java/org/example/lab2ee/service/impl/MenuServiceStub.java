package org.example.lab2ee.service.impl;

import org.example.lab2ee.model.MenuItem;
import org.example.lab2ee.service.MenuService;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class MenuServiceStub implements MenuService {

    private static final Map<Integer, MenuItem> menuItems = new LinkedHashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(12);

    static {
        add(1, "Брускета з томатами", "Хрустка чіабата з томатами черрі, базиліком та часниковим маслом", new BigDecimal("89.00"), MenuItem.Category.STARTERS, true, 210);
        add(2, "Крем-суп з гарбуза", "Ніжний гарбузовий суп-пюре зі вершками та насінням гарбуза", new BigDecimal("120.00"), MenuItem.Category.SOUPS, true, 180);
        add(3, "Борщ із пампушками", "Традиційний червоний борщ з пампушками та часником", new BigDecimal("145.00"), MenuItem.Category.SOUPS, true, 320);
        add(4, "Стейк рибай", "Соковитий стейк рибай 300г на грилі з картопляним пюре", new BigDecimal("680.00"), MenuItem.Category.MAIN_COURSE, true, 750);
        add(5, "Паста карбонара", "Класична паста з панчетою, яєчним жовтком та пармезаном", new BigDecimal("245.00"), MenuItem.Category.MAIN_COURSE, true, 520);
        add(6, "Лосось на грилі", "Філе атлантичного лосося з овочами гриль та соусом тартар", new BigDecimal("395.00"), MenuItem.Category.MAIN_COURSE, false, 420);
        add(7, "Курча по-київськи", "Традиційна котлета по-київськи з вершковим маслом та зеленню", new BigDecimal("285.00"), MenuItem.Category.MAIN_COURSE, true, 580);
        add(8, "Тірамісу", "Класичний італійський десерт із маскарпоне та кавою", new BigDecimal("135.00"), MenuItem.Category.DESSERTS, true, 380);
        add(9, "Чізкейк Нью-Йорк", "Ніжний сирний чізкейк на вершковому печиві з ягідним соусом", new BigDecimal("125.00"), MenuItem.Category.DESSERTS, true, 420);
        add(10, "Апельсиновий сік", "Сік із свіжих апельсинів 300мл", new BigDecimal("85.00"), MenuItem.Category.DRINKS, true, 110);
        add(11, "Лимонад домашній", "Освіжаючий лимонад з м'ятою та імбиром 500мл", new BigDecimal("75.00"), MenuItem.Category.DRINKS, true, 95);
        add(12, "Капучіно", "Ароматна кава з молочною піною 200мл", new BigDecimal("65.00"), MenuItem.Category.DRINKS, true, 85);
    }

    private static void add(int id, String name, String desc, BigDecimal price,
                            MenuItem.Category cat, boolean avail, int cal) {
        menuItems.put(id, new MenuItem(id, name, desc, price, cat, avail, cal));
    }

    @Override
    public List<MenuItem> getAllMenuItems() {
        return new ArrayList<>(menuItems.values());
    }

    @Override
    public List<MenuItem> getMenuItemsByCategory(MenuItem.Category category) {
        return menuItems.values().stream().filter(i -> i.getCategory() == category).collect(Collectors.toList());
    }

    @Override
    public List<MenuItem> getAvailableMenuItems() {
        return menuItems.values().stream().filter(MenuItem::isAvailable).collect(Collectors.toList());
    }

    @Override
    public Optional<MenuItem> getMenuItemById(int id) {
        return Optional.ofNullable(menuItems.get(id));
    }

    @Override
    public MenuItem createMenuItem(MenuItem item) {
        int newId = idCounter.incrementAndGet();
        item.setId(newId);
        menuItems.put(newId, item);
        return item;
    }

    @Override
    public MenuItem updateMenuItem(MenuItem item) {
        menuItems.put(item.getId(), item);
        return item;
    }

    @Override
    public boolean deleteMenuItem(int id) {
        return menuItems.remove(id) != null;
    }

    @Override
    public List<MenuItem.Category> getAllCategories() {
        return Arrays.asList(MenuItem.Category.values());
    }
}
