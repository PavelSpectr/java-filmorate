package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long genId = 1;

    @Override
    public User createUser(User user) {
        isValid(user);
        user.setId(genId++);
        users.put(user.getId(), user);
        log.debug("Пользователь успешно добавлен: {}", user.getName());
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (user.getId() == null || !users.containsKey(user.getId())) {
            throw new ValidationException("Такого пользователя не существует.");
        }
        isValid(user);
        users.put(user.getId(), user);
        log.debug("Пользователь успешно изменен: {}", user.getName());
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        log.debug("Текущее количество пользователей: {}", users.size());
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(Long userId) {
        if (users.get(userId) == null || !users.containsKey(userId)) {
            throw new ValidationException("Пользователь не найден");
        }
        log.debug("Пользователь с id={} успешно найден", userId);
        return users.get(userId);
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
        }
        if (user.getBirthday() == null || user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }
    }
}
