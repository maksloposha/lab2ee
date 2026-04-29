package org.example.lab2ee.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public enum Status {
        PENDING("Очікує"), CONFIRMED("Підтверджено"), PREPARING("Готується"),
        READY("Готово"), DELIVERED("Доставлено"), CANCELLED("Скасовано");

        private final String displayName;

        Status(String d) {
            this.displayName = d;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "customer_name" , nullable = false, length = 100)
    private String customerName;

    @Column(name = "customer_phone" , nullable = false, length = 20)
    private String customerPhone;

    @Column(name = "delivery_address" , nullable = false, columnDefinition = "TEXT")
    private String deliveryAddress;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Status status;

    @Column(name = "created_at" , nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at" , nullable = false)
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "order" ,
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();

    public Order() {
        this.status = Status.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Order(int id, String customerName, String customerPhone,
                 String deliveryAddress, Status status, LocalDateTime createdAt) {
        this();
        this.id = id;
        this.customerName = customerName;
        this.customerPhone = customerPhone;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.createdAt = createdAt;
    }

    public BigDecimal getTotal() {
        return items.stream().map(OrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int getTotalItems() {
        return items.stream().mapToInt(OrderItem::getQuantity).sum();
    }

    public void addItem(OrderItem item) {
        item.setOrder(this);
        items.add(item);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> i) {
        this.items = i;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status s) {
        this.status = s;
        this.updatedAt = LocalDateTime.now();
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
        this.updatedAt = d; }
}