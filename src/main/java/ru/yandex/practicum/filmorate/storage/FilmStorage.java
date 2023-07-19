package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getFilmById(Long filmId);

    List<Film> getFilms();

    Long addFilm(Film film);

    Long updateFilm(Film film);

    void deleteFilm(long filmId);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);
}