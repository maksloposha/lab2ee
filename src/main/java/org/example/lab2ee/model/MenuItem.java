package org.example.lab2ee.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "menu_items")
public class MenuItem implements Serializable {

    private static final long serialVersionUID = 1L;

    public enum Category {
        STARTERS("Закуски"),
        SOUPS("Супи"),
        MAIN_COURSE("Основні страви"),
        DESSERTS("Десерти"),
        DRINKS("Напої");

        private final String displayName;

        Category(String d) {
            this.displayName = d;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // SERIAL в PostgreSQL
    private int id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Category category;

    @Column(nullable = false)
    private boolean available;

    @Column(nullable = false)
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

    public void setName(String n) {
        this.name = n;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String d) {
        this.description = d;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal p) {
        this.price = p;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category c) {
        this.category = c;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean a) {
        this.available = a;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int c) {
        this.calories = c;
    }

}