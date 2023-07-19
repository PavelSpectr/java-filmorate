package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

@SpringBootTest
public class UserValidationTest {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private User user;

    @BeforeEach
    public void createValidUser() {
        user = new User();
        user.setEmail("user@mail.ru");
        user.setLogin("user");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));
    }

    @Test
    @DisplayName("Проверка валидации электронного адреса, когда он равен null")
    public void shouldFailValidationWhenEmailIsNull() {
        user.setEmail(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Проверка валидации электронного адреса, когда он имеет неверный формат")
    public void shouldFailValidationWhenEmailIsInvalid() {
        user.setEmail("mail.ru");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Проверка валидации логина, когда он равен null")
    public void shouldFailValidationWhenLoginIsNull() {
        user.setLogin(null);
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Проверка валидации логина, когда в он имеет неверный формат")
    public void shouldFailValidationWhenLoginIsInvalid() {
        user.setLogin("login with space");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

    @Test
    @DisplayName("Имя должно быть как логин, когда имя равно null")
    public void shouldReturnNameAsLoginWhenNameIsNull() {
        user.setName(null);
        Assertions.assertEquals(user.getLogin(), user.getName());
    }

    @Test
    @DisplayName("Проверка валидации даты рождения, когда дата в будущем")
    public void shouldFailValidationWhenBirthdayIsInFuture() {
        user.setBirthday(LocalDate.of(2999, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertFalse(violations.isEmpty());
    }

}