package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final FilmService filmService;

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        return userService.getUserById(userId);
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriendsByUserId(@PathVariable Long userId) {
        return userService.getFriendsByUserId(userId);
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        return userService.getCommonFriends(userId, friendId);
    }

    @GetMapping("/{userId}/feed")
    public List<UserEvent> getEventsByUserId(@PathVariable Long userId) {
        return userService.getEventsByUserId(userId);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        return userService.updateUser(user);
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") Long userId) {
        userService.deleteUser(userId);
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        userService.removeFriend(userId, friendId);
    }

    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable("id") Long userId) {
        log.info("Получен GET-запрос к эндпоинту: '/users' на получение рекоммендаций пользователю с ID={}", userId);
        return filmService.getRecommendations(userId);
    }
}