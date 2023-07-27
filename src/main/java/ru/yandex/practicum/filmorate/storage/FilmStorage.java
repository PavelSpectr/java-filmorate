package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film getFilmById(Long filmId);

    List<Film> getFilms();

    List<Film> getCommonFilms(long userId, long friendId);

    List<Film> getPopularFilms(int count, Long genreId, Integer year);

    Long addFilm(Film film);

    Long updateFilm(Film film);

    void deleteFilm(long filmId);

    void addLike(long filmId, long userId);

    void removeLike(long filmId, long userId);

    List<Film> getFilmsByDirector(long directorId, String sortBy);

    List<Film> getFilmsBySearchQuery(String query, List<String> by);

    List<Film> getRecommendations(Long userId);
}