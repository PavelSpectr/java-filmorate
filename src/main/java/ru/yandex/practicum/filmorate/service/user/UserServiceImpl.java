package ru.yandex.practicum.filmorate.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;

    @Override
    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    @Override
    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    @Override
    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    @Override
    public User getUserById(Long userId) {
        return userStorage.getUserById(userId);
    }

    @Override
    public void addFriend(Long userId, Long friendId) {
        Set<Long> user = userStorage.getUserById(userId).getFriends();
        Set<Long> friend = userStorage.getUserById(friendId).getFriends();
        if (user.contains(friendId) || friend.contains(userId)) {
            log.debug("Пользователь с id={} уже находится в друзьях у пользователя id={}", friendId, userId);
        }
        user.add(friendId);
        log.debug("Пользователь с id {} успешно добавлен в друзья пользователя id={}", friendId, userId);
        friend.add(userId);
        log.debug("Пользователь с id {} успешно добавлен в друзья пользователя id={}", userId, friendId);
    }

    @Override
    public void delFriend(Long userId, Long friendId) {
        Set<Long> user = userStorage.getUserById(userId).getFriends();
        Set<Long> friend = userStorage.getUserById(friendId).getFriends();
        if (user.contains(friendId) || friend.contains(userId)) {
            log.debug("Пользователь с id={} уже удален или отсутствует в друзьях пользователя id={}", friendId, userId);
        }
        user.remove(friendId);
        log.debug("Пользователь с id={} успешно удален из друзей пользователя id={}", friendId, userId);
        friend.remove(userId);
        log.debug("Пользователь с id={} успешно удален из друзей пользователя id={}", userId, friendId);
    }

    @Override
    public List<User> getFriends(Long userId) {
        User user = userStorage.getUserById(userId);
        if (user.getFriends().isEmpty()) {
            throw new ValidationException("Список друзей пуст");
        }
        List<User> friends = new ArrayList<>();
        for (Long friend : user.getFriends()) {
            friends.add(userStorage.getUserById(friend));
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(Long userId, Long friendId) {
        List<User> commonFriends = new ArrayList<>();
        Set<Long> user = userStorage.getUserById(userId).getFriends();
        Set<Long> friend = userStorage.getUserById(friendId).getFriends();
        for (Long along : user) {
            if (friend.contains(along)) {
                commonFriends.add(userStorage.getUserById(along));
            }
        }
        return commonFriends;
    }
}
