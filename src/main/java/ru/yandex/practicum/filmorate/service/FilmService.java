package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.model.UserEventOperation;
import ru.yandex.practicum.filmorate.model.UserEventType;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserEventStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserEventStorage eventStorage;

    private final UserStorage userStorage;

    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public List<Film> getCommonFilms(long userId, long friendId) {
        userStorage.getUserById(userId);
        return filmStorage.getCommonFilms(userId, friendId);
    }

    public Film addFilm(Film film) {
        return filmStorage.getFilmById(filmStorage.addFilm(film));
    }

    public Film updateFilm(Film film) {
        return filmStorage.getFilmById(filmStorage.updateFilm(film));
    }

    public void deleteFilm(Long filmId) {
        filmStorage.deleteFilm(filmId);
    }

    public void addLike(long filmId, long userId) {
        userStorage.getUserById(userId);
        filmStorage.addLike(filmId, userId);
        UserEvent userEvent = new UserEvent(UserEventType.LIKE, UserEventOperation.ADD, userId, filmId);
        eventStorage.addEvent(userEvent);
    }

    public void removeLike(long filmId, long userId) {
        userStorage.getUserById(userId);
        filmStorage.removeLike(filmId, userId);
        UserEvent userEvent = new UserEvent(UserEventType.LIKE, UserEventOperation.REMOVE, userId, filmId);
        eventStorage.addEvent(userEvent);
    }

    public List<Film> getMostPopularFilms(int count, Long genreId, Integer year) {
        return filmStorage.getPopularFilms(count, genreId, year);
    }

    public List<Film> getFilmsByDirector(long directorId, String sortBy) {
        return filmStorage.getFilmsByDirector(directorId, sortBy);
    }

    public List<Film> getFilmsBySearchQuery(String query, List<String> by) {
        return filmStorage.getFilmsBySearchQuery(query, by);
    }

    public List<Film> getRecommendations(Long userId) {
        userStorage.getUserById(userId);
        return filmStorage.getRecommendations(userId);
    }
}