package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);
    private long genId = 1;

    @Override
    public Film createFilm(Film film) {
        isValid(film);
        film.setId(genId++);
        films.put(film.getId(), film);
        log.debug("Фильм успешно добавлен: {}", film.getName());
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        if (film.getId() == null || !films.containsKey(film.getId())) {
            throw new ValidationException("Такого фильма не существует.");
        }
        isValid(film);
        films.put(film.getId(), film);
        log.debug("Фильм успешно изменен: {}", film.getName());
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        log.debug("Текущее количество фильмов в библиотеке: {}", films.size());
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(Long filmId) {
        if (films.get(filmId) == null || !films.containsKey(filmId)) {
            throw new ValidationException("Фильм не найден");
        }
        log.debug("Фильм с id {} успешно найден", filmId);
        return films.get(filmId);
    }

    private void isValid(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription() == null || film.getDescription().length() > 200)  {
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
            throw new ValidationException("Дата релиза — не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() == null || film.getDuration() < 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной.");
        }
    }
}
