package org.example.lab2ee.model;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public enum Role {
        USER("User"), ADMIN("Admin");
        private final String displayName;

        Role(String d) {
            this.displayName = d;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash" , nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "full_name" , nullable = false, length = 100)
    private String fullName;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<Order> orders = new ArrayList<>();

    public User() {
    }

    public User(int id, String username, String fullName,
                String email, String phone, Role role) {
        this.id = id;
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    public boolean isAdmin() {
        return Role.ADMIN.equals(role);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String u) {
        this.username = u;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String p) {
        this.passwordHash = p;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String f) {
        this.fullName = f;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String e) {
        this.email = e;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String p) {
        this.phone = p;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role r) {
        this.role = r;
    }
}