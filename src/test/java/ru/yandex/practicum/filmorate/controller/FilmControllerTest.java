package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    private FilmController filmController;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
    }

    @Test
    void createFilm() {
        Film createdFilm = filmController.createFilm(
                new Film("Поймай меня, если сможешь",
                        "Виртуозный аферист годами водит за нос ФБР. " +
                                "Хит Стивена Спилберга по реальным событиям с Леонардо ДиКаприо",
                        LocalDate.of(2002, 12,16),
                        141));
        assertNotNull(createdFilm.getId());
    }

    @Test
    void createInvalidNameFilm() {
        assertThrows(ValidationException.class, () -> filmController.createFilm(
                new Film(" ",
                        "Виртуозный аферист годами водит за нос ФБР. " +
                                "Хит Стивена Спилберга по реальным событиям с Леонардо ДиКаприо",
                        LocalDate.of(2002, 12,16),
                        141)));
    }

    @Test
    void createInvalidDescriptionFilm() {
        assertThrows(ValidationException.class, () -> filmController.createFilm(
                new Film("Поймай меня, если сможешь",
                        "Фрэнк Эбегнейл успел поработать врачом, " +
                                "адвокатом и пилотом на пассажирской авиалинии – " +
                                "и все это до достижения полного совершеннолетия в 21 год. " +
                                "Мастер в обмане и жульничестве, он также обладал искусством подделки документов, " +
                                "что в конечном счете принесло ему миллионы долларов, которые он получил по " +
                                "фальшивым чекам.\n" +
                                "\n" +
                                "Агент ФБР Карл Хэнрэтти отдал бы все, чтобы схватить Фрэнка и привлечь " +
                                "к ответственности за свои деяния, но Фрэнк всегда опережает его на шаг, " +
                                "заставляя продолжать погоню.",
                        LocalDate.of(2002, 12,16),
                        141)));
    }

    @Test
    void createInvalidDateFilm() {
        assertThrows(ValidationException.class, () -> filmController.createFilm(
                new Film("Поймай меня, если сможешь",
                        "Виртуозный аферист годами водит за нос ФБР. " +
                                "Хит Стивена Спилберга по реальным событиям с Леонардо ДиКаприо",
                        LocalDate.of(1895, 12,27),
                        141)));
    }

    @Test
    void createInvalidDurationFilm() {
        assertThrows(ValidationException.class, () -> filmController.createFilm(
                new Film("Поймай меня, если сможешь",
                        "Виртуозный аферист годами водит за нос ФБР. " +
                                "Хит Стивена Спилберга по реальным событиям с Леонардо ДиКаприо",
                        LocalDate.of(2002, 12,16),
                        -1)));
    }
}