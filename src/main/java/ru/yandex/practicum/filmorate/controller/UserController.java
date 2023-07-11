package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        log.debug("Try to create user:");
        return userService.createUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) {
        log.debug("Try to update user:");
        return userService.updateUser(user);
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.debug("Try to get all users:");
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable("id") Long userId) {
        log.debug("Try to get user by id:");
        return userService.getUserById(userId);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        log.debug("Try to add friend:");
        userService.addFriend(userId, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void delFriend(@PathVariable("id") Long userId, @PathVariable Long friendId) {
        log.debug("Try to delete friend:");
        userService.delFriend(userId, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable("id") Long userId) {
        log.debug("Try to get friends:");
        return userService.getFriends(userId);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Long userId, @PathVariable Long otherId) {
        log.debug("Try to get common friends:");
        return userService.getCommonFriends(userId, otherId);
    }
}

