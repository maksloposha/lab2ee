package org.example.lab2ee.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.math.BigDecimal;

/**
 * Class-level validator for @ValidPriceRange.
 * <p>
 * Business rule: якщо calories > 0, то ціна теж має бути > 0.
 * Демонструє крос-польову (cross-field) валідацію на рівні об'єкта.
 */
public class ValidPriceRangeValidator implements ConstraintValidator<ValidPriceRange, Object> {

    private String priceFieldName;
    private String caloriesFieldName;

    @Override
    public void initialize(ValidPriceRange annotation) {
        this.priceFieldName = annotation.minField();
        this.caloriesFieldName = annotation.maxField();
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (obj == null) return true;

        try {
            Field priceField = obj.getClass().getDeclaredField(priceFieldName);
            Field caloriesField = obj.getClass().getDeclaredField(caloriesFieldName);
            priceField.setAccessible(true);
            caloriesField.setAccessible(true);

            Object priceVal = priceField.get(obj);
            Object caloriesVal = caloriesField.get(obj);

            // If both present: calories > 0 → price must be > 0
            if (priceVal instanceof BigDecimal && caloriesVal instanceof Integer) {
                BigDecimal price = (BigDecimal) priceVal;
                Integer calories = (Integer) caloriesVal;

                if (calories > 0 && price.compareTo(BigDecimal.ZERO) <= 0) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                            "Якщо вказані калорії > 0, ціна має бути більше 0"
                    ).addPropertyNode(priceFieldName).addConstraintViolation();
                    return false;
                }
            }
            return true;

        } catch (NoSuchFieldException | IllegalAccessException e) {
            return true;
        }
    }
}
