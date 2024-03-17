package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.model.UserEventOperation;
import ru.yandex.practicum.filmorate.model.UserEventType;
import ru.yandex.practicum.filmorate.storage.UserEventStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;
    private final UserEventStorage eventStorage;

    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User createUser(User user) {
        return userStorage.getUserById(userStorage.createUser(user));
    }

    public User updateUser(User user) {
        return userStorage.getUserById(userStorage.updateUser(user));
    }

    public void deleteUser(Long userId) {
        userStorage.deleteUser(userId);
    }

    public void addFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        userStorage.addFriend(user.getId(), friend.getId());
        UserEvent userEvent = new UserEvent(UserEventType.FRIEND, UserEventOperation.ADD, userId, friendId);
        eventStorage.addEvent(userEvent);
    }

    public void removeFriend(long userId, long friendId) {
        User user = getUserById(userId);
        User friend = getUserById(friendId);
        userStorage.removeFriend(user.getId(), friend.getId());
        UserEvent userEvent = new UserEvent(UserEventType.FRIEND, UserEventOperation.REMOVE, userId, friendId);
        eventStorage.addEvent(userEvent);
    }

    public List<User> getCommonFriends(long userId, long friendId) {
        final List<User> friends = getFriendsByUserId(userId);
        friends.retainAll(getFriendsByUserId(friendId));
        return friends;
    }

    public List<User> getFriendsByUserId(long userId) {
        return getUserById(userId).getFriendIds().stream()
                .map(this::getUserById)
                .collect(Collectors.toList());
    }

    public List<UserEvent> getEventsByUserId(long userId) {
        return eventStorage.getEventsByUserId(getUserById(userId).getId());
    }
}