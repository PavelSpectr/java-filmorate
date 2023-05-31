package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int genId = 1;

    @PostMapping
    public Film createFilm(@RequestBody Film film) {
        isValid(film);
        film.setId(genId++);
        films.put(film.getId(), film);
        log.debug("Фильм успешно добавлен: {}", film.getName());
        return film;
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film film) {
        for (Integer id : films.keySet()) {
            if (!Objects.equals(film.getId(), id)) {
                throw new ValidationException("Такого фильма не существует.");
            }
        }
        isValid(film);
        films.put(film.getId(), film);
        log.debug("Фильм успешно изменен: {}", film.getName());
        return film;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.debug("Текущее количество фильмов в библиотеке: {}", films.size());
        return new ArrayList<>(films.values());
    }

    private void isValid(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription().length() > 200)  {
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза — не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() < 1) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}
