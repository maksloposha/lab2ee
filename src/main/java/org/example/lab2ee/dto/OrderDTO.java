package org.example.lab2ee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.lab2ee.model.Order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderDTO {

    private Integer id;
    private String customerName;
    private String customerPhone;
    private String deliveryAddress;
    private String notes;
    private String status;
    private String statusDisplay;
    private BigDecimal total;
    private Integer totalItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItemDTO> items;

    public OrderDTO() {
    }

    public static OrderDTO fromModel(Order o) {
        OrderDTO dto = new OrderDTO();
        dto.id = o.getId();
        dto.customerName = o.getCustomerName();
        dto.customerPhone = o.getCustomerPhone();
        dto.deliveryAddress = o.getDeliveryAddress();
        dto.notes = o.getNotes();
        dto.status = o.getStatus() != null ? o.getStatus().name() : null;
        dto.statusDisplay = o.getStatus() != null ? o.getStatus().getDisplayName() : null;
        dto.total = o.getTotal();
        dto.totalItems = o.getTotalItems();
        dto.createdAt = o.getCreatedAt();
        dto.updatedAt = o.getUpdatedAt();
        if (o.getItems() != null) {
            dto.items = o.getItems().stream().map(OrderItemDTO::fromModel).collect(Collectors.toList());
        }
        return dto;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String s) {
        this.status = s;
    }

    public String getStatusDisplay() {
        return statusDisplay;
    }

    public void setStatusDisplay(String s) {
        this.statusDisplay = s;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal t) {
        this.total = t;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer t) {
        this.totalItems = t;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime d) {
        this.createdAt = d;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime d) {
        this.updatedAt = d;
    }

    public List<OrderItemDTO> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDTO> i) {
        this.items = i;
    }
}
