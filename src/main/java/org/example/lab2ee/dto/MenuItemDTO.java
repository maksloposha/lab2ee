package org.example.lab2ee.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import org.example.lab2ee.model.MenuItem;

import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuItemDTO {

    private Integer id;
    private String name;
    private String description;
    private BigDecimal price;
    private String category;
    private String categoryDisplay;
    private Boolean available;
    private Integer calories;

    public MenuItemDTO() {
    }

    public static MenuItemDTO fromModel(MenuItem m) {
        MenuItemDTO dto = new MenuItemDTO();
        dto.id = m.getId();
        dto.name = m.getName();
        dto.description = m.getDescription();
        dto.price = m.getPrice();
        dto.category = m.getCategory() != null ? m.getCategory().name() : null;
        dto.categoryDisplay = m.getCategory() != null ? m.getCategory().getDisplayName() : null;
        dto.available = m.isAvailable();
        dto.calories = m.getCalories();
        return dto;
    }

    public MenuItem toModel() {
        MenuItem m = new MenuItem();
        if (id != null) m.setId(id);
        if (name != null) m.setName(name);
        if (description != null) m.setDescription(description);
        if (price != null) m.setPrice(price);
        if (available != null) m.setAvailable(available);
        if (calories != null) m.setCalories(calories);
        if (category != null) {
            try {
                m.setCategory(MenuItem.Category.valueOf(category));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return m;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String c) {
        this.category = c;
    }

    public String getCategoryDisplay() {
        return categoryDisplay;
    }

    public void setCategoryDisplay(String c) {
        this.categoryDisplay = c;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean a) {
        this.available = a;
    }


    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer c) {
        this.calories = c;
    }
}
