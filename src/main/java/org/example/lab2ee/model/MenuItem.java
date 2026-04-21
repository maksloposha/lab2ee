package org.example.lab2ee.model;

import java.math.BigDecimal;


public class MenuItem {

    private int id;
    private String name;
    private String description;
    private BigDecimal price;
    private Category category;
    private boolean available;
    private int calories;
    public MenuItem() {
    }

    public MenuItem(int id, String name, String description, BigDecimal price,
                    Category category, boolean available, int calories) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.available = available;
        this.calories = calories;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public enum Category {
        STARTERS("Закуски"),
        SOUPS("Супи"),
        MAIN_COURSE("Основні страви"),
        DESSERTS("Десерти"),
        DRINKS("Напої");

        private final String displayName;

        Category(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}