package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User getUserById(Long userId) {
        log.debug("+ getUserById: userId={}", userId);
        User user = userStorage.getUserById(userId);
        log.debug("- getUserById: {}", user);
        return user;
    }

    public List<User> getUsers() {
        log.debug("+ getUsers");
        List<User> users = userStorage.getUsers();
        log.debug("- getUsers: {}", users);
        return users;
    }

    public User createUser(User user) {
        log.debug("+ createUser: {}", user);
        User createdUser = userStorage.getUserById(userStorage.createUser(user));
        log.debug("- createUser: {}", createdUser);
        return createdUser;
    }

    public User updateUser(User user) {
        log.debug("+ updateUser: {}", user);
        User updatedUser = userStorage.getUserById(userStorage.updateUser(user));
        log.debug("- updateUser: {}", updatedUser);
        return updatedUser;
    }

    public void deleteUser(Long userId) {
        log.debug("+ deleteUser: userId={}", userId);
        userStorage.deleteUser(userId);
        log.debug("- deleteUser");
    }

    public void addFriend(long userId, long friendId) {
        log.debug("+ addFriend: userId={}, friendId={}", userId, friendId);
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        userStorage.addFriend(user.getId(), friend.getId());
        log.debug("- addFriend");
    }

    public void removeFriend(long userId, long friendId) {
        log.debug("+ removeFriend: userId={}, friendId={}", userId, friendId);
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        userStorage.removeFriend(user.getId(), friend.getId());
        log.debug("- removeFriend");
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        log.debug("+ getCommonFriends: userId={}, friendId={}", userId, friendId);
        final List<User> friends = getFriendsByUserId(userId);
        friends.retainAll(getFriendsByUserId(friendId));
        log.debug("- getCommonFriends: {}", friends);

        return friends;
    }

    public List<User> getFriendsByUserId(long userId) {
        log.debug("+ getFriendsByUserId: userId={}", userId);

        List<User> friends = getUserById(userId).getFriendIds().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());

        log.debug("- getFriendsByUserId: {}", friends);

        return friends;
    }
}