package org.example.lab2ee.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.util.List;

/**
 * Request body for POST /api/orders and PUT /api/orders/{id}.
 * <p>
 * Uses standard Bean Validation annotations.
 * The nested OrderItemRequest is validated recursively via @Valid.
 */
public class CreateOrderRequest {

    @NotBlank(message = "Ім'я замовника є обов'язковим")
    @Size(min = 2, max = 100, message = "Ім'я має бути від 2 до 100 символів")
    private String customerName;

    @NotBlank(message = "Номер телефону є обов'язковим")
    @Pattern(
            regexp = "^\\+?[0-9]{10,15}$",
            message = "Телефон має бути у форматі +380XXXXXXXXX або 0XXXXXXXXX"
    )
    private String customerPhone;

    @NotBlank(message = "Адреса доставки є обов'язковою")
    @Size(min = 5, max = 300, message = "Адреса має бути від 5 до 300 символів")
    private String deliveryAddress;

    @Size(max = 500, message = "Нотатки не можуть перевищувати 500 символів")
    private String notes;

    @NotNull(message = "Список позицій не може бути порожнім")
    @Size(min = 1, message = "Замовлення повинно містити хоча б одну позицію")
    private List<@Valid OrderItemRequest> items;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String n) {
        this.customerName = n;
    }

    public String getCustomerPhone() {
        return customerPhone;
    }

    public void setCustomerPhone(String p) {
        this.customerPhone = p;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String a) {
        this.deliveryAddress = a;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String n) {
        this.notes = n;
    }

    public List<OrderItemRequest> getItems() {
        return items;
    }

    public void setItems(List<OrderItemRequest> i) {
        this.items = i;
    }

    // ── Nested DTO ────────────────────────────────────────────────────────────
    public static class OrderItemRequest {

        @NotNull(message = "ID елемента меню є обов'язковим")
        @Min(value = 1, message = "ID елемента меню має бути позитивним числом")
        private Integer menuItemId;

        @NotNull(message = "Кількість є обов'язковою")
        @Min(value = 1, message = "Кількість має бути не менше 1")
        @Max(value = 50, message = "Кількість не може перевищувати 50")
        private Integer quantity;

        @Size(max = 200, message = "Особливі побажання не можуть перевищувати 200 символів")
        private String specialInstructions;

        public Integer getMenuItemId() {
            return menuItemId;
        }

        public void setMenuItemId(Integer id) {
            this.menuItemId = id;
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
    }
}
