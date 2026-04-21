package org.example.lab2ee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.lab2ee.model.OrderItem;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderItemDTO {

    private Integer menuItemId;
    private String menuItemName;
    private BigDecimal menuItemPrice;
    private Integer quantity;
    private String specialInstructions;
    private BigDecimal subtotal;

    public OrderItemDTO() {
    }

    public static OrderItemDTO fromModel(OrderItem oi) {
        OrderItemDTO dto = new OrderItemDTO();
        dto.quantity = oi.getQuantity();
        dto.specialInstructions = oi.getSpecialInstructions();
        dto.subtotal = oi.getSubtotal();
        if (oi.getMenuItem() != null) {
            dto.menuItemId = oi.getMenuItem().getId();
            dto.menuItemName = oi.getMenuItem().getName();
            dto.menuItemPrice = oi.getMenuItem().getPrice();
        }
        return dto;
    }

    public Integer getMenuItemId() {
        return menuItemId;
    }

    public void setMenuItemId(Integer id) {
        this.menuItemId = id;
    }

    public String getMenuItemName() {
        return menuItemName;
    }

    public void setMenuItemName(String n) {
        this.menuItemName = n;
    }

    public BigDecimal getMenuItemPrice() {
        return menuItemPrice;
    }

    public void setMenuItemPrice(BigDecimal p) {
        this.menuItemPrice = p;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer q) {
        this.quantity = q;
    }

    public String getSpecialInstructions() {
        return specialInstructions;
    }

    public void setSpecialInstructions(String s) {
        this.specialInstructions = s;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal s) {
        this.subtotal = s;
    }
}
