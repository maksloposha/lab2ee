package org.example.lab2ee.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


@Documented
@Constraint(validatedBy = ValidPriceRangeValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPriceRange {
    String message() default "Некоректне співвідношення ціни та калорій";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String minField() default "price";

    String maxField() default "calories";
}
