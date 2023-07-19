package ru.yandex.practicum.filmorate.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class FilmMinReleaseDateValidator implements ConstraintValidator<FilmMinReleaseDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate minDate = LocalDate.of(1895, 12, 28);
        return value.isEqual(minDate) || value.isAfter(minDate);
    }
}
