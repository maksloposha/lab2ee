package org.example.lab2ee.dto;

import jakarta.validation.constraints.*;
import org.example.lab2ee.validation.ValidCategory;
import org.example.lab2ee.validation.ValidPriceRange;

import java.math.BigDecimal;


@ValidPriceRange(
        minField = "price",
        maxField = "calories",
        message = "Якщо вказані калорії > 0, ціна має бути більше 0"
)
public class CreateMenuItemRequest {

    @NotBlank(message = "Назва страви не може бути порожньою")
    @Size(min = 2, max = 100, message = "Назва має бути від 2 до 100 символів")
    private String name;

    @Size(max = 500, message = "Опис не може перевищувати 500 символів")
    private String description;

    @NotNull(message = "Ціна є обов'язковою")
    @DecimalMin(value = "0.01", message = "Ціна має бути більше 0")
    @DecimalMax(value = "99999.99", message = "Ціна не може перевищувати 99 999.99 ₴")
    private BigDecimal price;

    @NotBlank(message = "Категорія є обов'язковою")
    @ValidCategory(message = "Невідома категорія. Допустимі: STARTERS, SOUPS, MAIN_COURSE, DESSERTS, DRINKS")
    private String category;

    private Boolean available = true;

    @Size(max = 10, message = "Емодзі не може перевищувати 10 символів")
    private String imageUrl;

    @Min(value = 0, message = "Калорії не можуть бути від'ємними")
    @Max(value = 9999, message = "Калорії не можуть перевищувати 9999")
    private Integer calories = 0;

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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean a) {
        this.available = a;
    }

    public String getImageUrl() {
        return imageUrl;
    }


    public Integer getCalories() {
        return calories;
    }

    public void setCalories(Integer c) {
        this.calories = c;
    }
}
