package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film createFilm(Film film);
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    Film updateFilm(Film film);
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    List<Film> getAllFilms();
    @SuppressWarnings("checkstyle:EmptyLineSeparator")
    Film getFilmById(Long filmId);
}
