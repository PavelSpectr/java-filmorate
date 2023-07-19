package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
public class FilmValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private Film film;

    @BeforeEach
    public void createValidFilm() {
        film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.of(2000, 1, 1));
        film.setDuration(120);
    }

    @Test
    @DisplayName("Проверка валидации названия фильма, когда оно равно null")
    public void shouldFailValidationWhenNameIsNull() {
        film.setName(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Проверка валидации описания фильма, когда оно больше 200 символов")
    public void shouldFailValidationWhenDescriptionIsTooLong() {
        film.setDescription(new String(new char[201]).replace("\0", "d"));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Проверка валидации даты релиза, когда дата раньше 28.12.1895")
    public void shouldFailValidationWhenReleaseDateIsInPast() {
        film.setReleaseDate(LocalDate.of(1895, 12, 27));
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Проверка валидации продолжительности фильма, когда она отрицательная")
    public void shouldFailValidationWhenDurationIsNegative() {
        film.setDuration(-1);
        Set<ConstraintViolation<Film>> violations = validator.validate(film);
        Assertions.assertFalse(violations.isEmpty());
    }

}