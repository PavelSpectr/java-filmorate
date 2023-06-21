package ru.yandex.practicum.filmorate.service.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Set;

public interface UserService {

    public void addFriend(Long user, Long friend);

    public void delFriend(Long user, Long friend);

    User createUser(User user);

    User updateUser(User user);

    List<User> getAllUsers();

    User getUserById(Long userId);

    List<User> getFriends(Long userId);

    List<User> getCommonFriends(Long userId, Long friendId);
}
