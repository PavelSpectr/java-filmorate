package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserStorageTest {
    private final UserDbStorageImpl userStorage;

    @Test
    @DirtiesContext
    @DisplayName("Возврат пользователя по идентификатору")
    public void shouldReturnUserById() {
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setLogin("user");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Long addedUserId = userStorage.createUser(user);
        User retrievedUser = userStorage.getUserById(addedUserId);

        Assertions.assertNotNull(retrievedUser);

        Assertions.assertEquals(user.getEmail(), retrievedUser.getEmail());
        Assertions.assertEquals(user.getLogin(), retrievedUser.getLogin());
        Assertions.assertEquals(user.getName(), retrievedUser.getName());
        Assertions.assertEquals(user.getBirthday(), retrievedUser.getBirthday());
    }

    @Test
    @DirtiesContext
    @DisplayName("Добавление пользователя")
    public void shouldAddAndReturnUser() {
        User user1 = new User();
        user1.setEmail("user1@mail.ru");
        user1.setLogin("user1");
        user1.setName("Name 1");
        user1.setBirthday(LocalDate.of(2000, 1, 1));

        User user2 = new User();
        user2.setEmail("user2@mail.ru");
        user2.setLogin("user2");
        user2.setName("Name 2");
        user2.setBirthday(LocalDate.of(2000, 2, 1));


        userStorage.createUser(user1);
        userStorage.createUser(user2);

        List<User> users = userStorage.getUsers();

        Assertions.assertNotNull(users);
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertEquals(2, users.size());

        User retrievedUser1 = users.get(0);
        Assertions.assertEquals(user1.getEmail(), retrievedUser1.getEmail());
        Assertions.assertEquals(user1.getLogin(), retrievedUser1.getLogin());
        Assertions.assertEquals(user1.getName(), retrievedUser1.getName());
        Assertions.assertEquals(user1.getBirthday(), retrievedUser1.getBirthday());

        User retrievedUser2 = users.get(1);
        Assertions.assertEquals(user2.getEmail(), retrievedUser2.getEmail());
        Assertions.assertEquals(user2.getLogin(), retrievedUser2.getLogin());
        Assertions.assertEquals(user2.getName(), retrievedUser2.getName());
        Assertions.assertEquals(user2.getBirthday(), retrievedUser2.getBirthday());
    }

    @Test
    @DirtiesContext
    @DisplayName("Обновление пользователя")
    public void shouldUpdateUser() {
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setLogin("user");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        User addedUser = userStorage.getUserById(userStorage.createUser(user));

        addedUser.setEmail("updated-user@mail.ru");
        addedUser.setLogin("updatedUser");
        addedUser.setName("Updated Name");
        addedUser.setBirthday(LocalDate.of(2002, 3, 2));

        User updatedUser = userStorage.getUserById(userStorage.updateUser(addedUser));

        Assertions.assertNotNull(updatedUser);
        Assertions.assertEquals(addedUser.getEmail(), updatedUser.getEmail());
        Assertions.assertEquals(addedUser.getLogin(), updatedUser.getLogin());
        Assertions.assertEquals(addedUser.getName(), updatedUser.getName());
        Assertions.assertEquals(addedUser.getBirthday(), updatedUser.getBirthday());
    }

    @Test
    @DirtiesContext
    @DisplayName("Удаление пользователя")
    public void shouldDeleteUser() {
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setLogin("user");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));

        Long addedUserId = userStorage.createUser(user);

        userStorage.deleteUser(addedUserId);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> userStorage.getUserById(addedUserId)
        );
        Assertions.assertEquals("Пользователь #1 не найден.", exception.getMessage());

    }

    @Test
    @DirtiesContext
    @DisplayName("Добавление в друзья")
    public void shouldAddFriend() {
        User user = new User();
        user.setEmail("user1mail.ru");
        user.setLogin("user");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        long userId = userStorage.createUser(user);

        User friend = new User();
        friend.setEmail("friend@mail.ru");
        friend.setLogin("friend");
        friend.setName("Friend Name");
        friend.setBirthday(LocalDate.of(2000, 2, 1));
        long friendId = userStorage.createUser(friend);

        userStorage.addFriend(userId, friendId);

        User retrievedUser = userStorage.getUserById(userId);

        Assertions.assertNotNull(retrievedUser);
        Assertions.assertTrue(retrievedUser.getFriendIds().contains(friendId));
    }

    @Test
    @DirtiesContext
    @DisplayName("Удаление из друзей")
    public void shouldRemoveFriend() {
        User user = new User();
        user.setEmail("user1mail.ru");
        user.setLogin("user");
        user.setName("Name");
        user.setBirthday(LocalDate.of(2000, 1, 1));
        long userId = userStorage.createUser(user);

        User friend = new User();
        friend.setEmail("friend@mail.ru");
        friend.setLogin("friend");
        friend.setName("Friend Name");
        friend.setBirthday(LocalDate.of(2000, 2, 1));
        long friendId = userStorage.createUser(friend);

        userStorage.addFriend(userId, friendId);
        userStorage.removeFriend(userId, friendId);

        User retrievedUser = userStorage.getUserById(userId);

        Assertions.assertNotNull(retrievedUser);
        Assertions.assertFalse(retrievedUser.getFriendIds().contains(friendId));
    }
}