package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class IsAfterValidation implements ConstraintValidator<IsAfter, LocalDate> {
    private LocalDate date;
    @Override
    public void initialize(IsAfter constraintAnnotation) {
        //ConstraintValidator.super.initialize(constraintAnnotation);
        date = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if(value == null) {
            return true;
        }
        return value.isAfter(date);
    }
}
