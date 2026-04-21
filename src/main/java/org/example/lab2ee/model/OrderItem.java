package org.example.lab2ee.model;

import java.math.BigDecimal;


public class OrderItem {

    private int id;
    private MenuItem menuItem;
    private int quantity;
    private String specialInstructions;

    public OrderItem() {
    }

    public OrderItem(int id, MenuItem menuItem, int quantity, String specialInstructions) {
        this.id = id;
        this.menuItem = menuItem;
        this.quantity = quantity;
        this.specialInstructions = specialInstructions;
    }

    public BigDecimal getSubtotal() {
        if (menuItem == null || menuItem.getPrice() == null) return BigDecimal.ZERO;
        return menuItem.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String specialInstructions) {
        this.specialInstructions = specialInstructions;
    }
}