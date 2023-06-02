package ru.yandex.practicum.filmorate.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class AfterFirstFilmDateValidation implements ConstraintValidator<AfterFirstFilmDate, LocalDate> {
    private LocalDate date;

    @Override
    public void initialize(AfterFirstFilmDate constraintAnnotation) {
        date = LocalDate.parse(constraintAnnotation.value());
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.isAfter(date);
    }
}
