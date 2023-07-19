package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryUserStorageImpl implements UserStorage {
    private final Map<Long, User> usersData = new HashMap<>();

    @Override
    public User getUserById(Long userId) {
        if (!usersData.containsKey(userId)) {
            log.error("Пользователь #" + userId + " не найден.");
            throw new NotFoundException("Пользователь #" + userId + " не найден.");
        }

        return usersData.get(userId);
    }

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(usersData.values());
    }

    @Override
    public Long createUser(User user) {
        final Long id = usersData.size() + 1L;
        user.setId(id);
        usersData.put(id, user);
        return user.getId();
    }

    @Override
    public Long updateUser(User updatedUser) {
        final Long userId = updatedUser.getId();

        if (!usersData.containsKey(userId)) {
            log.error("Пользователь #" + userId + " не найден.");
            throw new NotFoundException("Пользователь #" + userId + " не найден.");
        }

        User user = usersData.get(userId);

        user.setEmail(updatedUser.getEmail());
        user.setLogin(updatedUser.getLogin());
        user.setName(updatedUser.getName());
        user.setBirthday(updatedUser.getBirthday());
        user.setFriendIds(updatedUser.getFriendIds());

        return userId;
    }

    @Override
    public void deleteUser(long userId) {
        usersData.remove(userId);
    }

    @Override
    public void addFriend(long userId, long friendId) {
        getUserById(userId).getFriendIds().add(friendId);
        getUserById(friendId).getFriendIds().add(userId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        getUserById(userId).getFriendIds().remove(friendId);
        getUserById(friendId).getFriendIds().remove(userId);
    }
}
