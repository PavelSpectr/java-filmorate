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
        log.info("+ getUsers");
        List<User> users = userService.getUsers();
        log.info("- getUsers: {}", users);
        return users;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable Long userId) {
        log.info("+ getUserById: userId={}", userId);
        User user = userService.getUserById(userId);
        log.info("- getUserById: {}", user);
        return user;
    }

    @GetMapping("/{userId}/friends")
    public List<User> getFriendsByUserId(@PathVariable Long userId) {
        log.info("+ getFriendsByUserId: userId={}", userId);
        List<User> friends = userService.getFriendsByUserId(userId);
        log.info("- getFriendsByUserId: {}", friends);
        return friends;
    }

    @GetMapping("/{userId}/friends/common/{friendId}")
    public List<User> getCommonFriends(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("+ getCommonFriends: userId={}, friendId={}", userId, friendId);
        List<User> friends = userService.getCommonFriends(userId, friendId);
        log.info("- getCommonFriends: {}", friends);
        return friends;
    }

    @GetMapping("/{userId}/feed")
    public List<UserEvent> getEventsByUserId(@PathVariable Long userId) {
        log.info("+ getEventsByUserId: userId={}", userId);
        List<UserEvent> events = userService.getEventsByUserId(userId);
        log.info("- getEventsByUserId: {}", events);
        return events;
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.info("+ createUser: {}", user);
        User createdUser = userService.createUser(user);
        log.info("- createUser: {}", createdUser);
        return createdUser;
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.info("+ updateUser: {}", user);
        User updatedUser = userService.updateUser(user);
        log.info("- updateUser: {}", updatedUser);
        return updatedUser;
    }

    @DeleteMapping("/{userId}")
    public void delete(@PathVariable("userId") Long userId) {
        log.info("+ deleteUser: userId={}", userId);
        userService.deleteUser(userId);
        log.info("- deleteUser");
    }

    @PutMapping("/{userId}/friends/{friendId}")
    public void addFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("+ addFriend: userId={}, friendId={}", userId, friendId);
        userService.addFriend(userId, friendId);
        log.info("- addFriend");
    }

    @DeleteMapping("/{userId}/friends/{friendId}")
    public void removeFriend(@PathVariable Long userId, @PathVariable Long friendId) {
        log.info("+ removeFriend: userId={}, friendId={}", userId, friendId);
        userService.removeFriend(userId, friendId);
        log.info("- removeFriend");
    }

    @GetMapping("/{id}/recommendations")
    public List<Film> getRecommendations(@PathVariable("id") Long userId) {
        log.info("+ getRecommendations: userId={}", userId);
        List<Film> films = filmService.getRecommendations(userId);
        log.info("- getRecommendations: {}", films);
        return films;
    }
}