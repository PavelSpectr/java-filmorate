package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User createUser(User user);
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    User updateUser(User user);
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    List<User> getAllUsers();
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    User getUserById(Long userId);
}
