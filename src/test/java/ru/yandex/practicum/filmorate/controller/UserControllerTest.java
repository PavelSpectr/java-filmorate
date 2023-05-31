package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;

    @BeforeEach
    public void setUp() {
        userController = new UserController();
    }
    @Test
    void createUser() {
        User createdUser = userController.createUser(
                new User("dunkan@cloud.com", "DunkanMcCloud", "Dunkan", LocalDate.of(1986, 9,28)));
        assertNotNull(createdUser.getId());
    }

    @Test
    void createInvalidEmailUser() {
        //assertThrows(ValidationException.class, () -> userController.createUser(new User("sdidjf kdjnsf")));
        try {
            userController.createUser(new User(" ", " ", " ", LocalDate.now().plusDays(7)));
        } catch (ValidationException e) {
            assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", e.getMessage());
            return;
        }
        fail();
    }

    @Test
    void createInvalidLoginUser() {
        try {
            userController.createUser(new User("dunkan@clod.com", " ", " ", LocalDate.now().plusDays(7)));
        } catch (ValidationException e) {
            assertEquals("Логин не может быть пустым и содержать пробелы.", e.getMessage());
            return;
        }
        fail();
    }

    @Test
    void createInvalidNameUser() {
        User createdUser = userController.createUser(
                new User("dunkan@cloud.com", "DunkanMcCloud", " ", LocalDate.of(1986, 9,28)));
        assertEquals(createdUser.getLogin(), createdUser.getName());
    }

    @Test
    void createInvalidBirthdayUser() {
        try {
            userController.createUser(new User("dunkan@clod.com", "DunkanMcCloud", "Dunkan", LocalDate.now().plusDays(7)));
        } catch (ValidationException e) {
            assertEquals("Дата рождения не может быть в будущем.", e.getMessage());
            return;
        }
        fail();
    }
}