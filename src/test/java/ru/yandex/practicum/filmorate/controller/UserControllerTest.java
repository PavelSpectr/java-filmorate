/*
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    //private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController(new UserService() {
        };


    @Test
    void createUser() {
        User createdUser = userController.createUser(
                new User("dunkan@cloud.com", "DunkanMcCloud", "Dunkan", LocalDate.of(1986, 9,28)));
        assertNotNull(createdUser.getId());
    }

    @Test
    void createInvalidEmailUser() {
        assertThrows(ValidationException.class, () -> userController.createUser(
                new User(" ", " ", " ", LocalDate.now().plusDays(1))));
    }

    @Test
    void createInvalidLoginUser() {
        assertThrows(ValidationException.class, () -> userController.createUser(
                new User("dunkan@clod.com", " ", " ", LocalDate.now().plusDays(7))));
    }

    @Test
    void createInvalidNameUser() {
        User createdUser = userController.createUser(
                new User("dunkan@cloud.com", "DunkanMcCloud", " ", LocalDate.of(1986, 9,28)));
        assertEquals(createdUser.getLogin(), createdUser.getName());
    }

    @Test
    void createInvalidBirthdayUser() {
        assertThrows(ValidationException.class, () -> userController.createUser(
                new User("dunkan@clod.com", "DunkanMcCloud", "Dunkan", LocalDate.now().plusDays(1))));
    }
}*/
