package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {

    private final Map<Integer, User> users = new HashMap<>();
    private int genId = 1;

    @PostMapping
    public User createUser(@RequestBody User user) {
        isValid(user);
        user.setId(genId++);
        users.put(user.getId(), user);
        log.debug("Пользователь успешно добавлен: {}", user.getName());
        return user;
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        for (Integer id : users.keySet()) {
            if (!Objects.equals(user.getId(), id)) {
                throw new ValidationException("Такого пользователя не существует.");
            }
        }
        isValid(user);
        users.put(user.getId(), user);
        log.debug("Пользователь успешно изменен: {}", user.getName());
        return user;
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    private void isValid(User user) {
        if (user.getEmail() == null || user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
            //throw new ValidationException("Имя для отображения может быть пустым — в таком случае будет использован логин.");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}

