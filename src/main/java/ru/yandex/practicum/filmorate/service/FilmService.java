package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.UserEventOperation;
import ru.yandex.practicum.filmorate.model.UserEventType;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserEventStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserEventStorage eventStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userEventDbStorage") UserEventStorage eventStorage) {
        this.filmStorage = filmStorage;
        this.eventStorage = eventStorage;
    }

    public Film getFilmById(Long filmId) {
        log.debug("+ getFilmById: filmId={}", filmId);
        Film film = filmStorage.getFilmById(filmId);
        log.debug("- getFilmById: {}", film);
        return film;
    }

    public List<Film> getFilms() {
        log.debug("+ getFilms");
        List<Film> films = filmStorage.getFilms();
        log.debug("- getFilms: {}", films);
        return films;
    }

    public List<Film> getCommonFilms(long userId, long friendId) {
        log.debug("+ getCommonFilms: userId={}, friendId={}", userId, friendId);
        List<Film> films = filmStorage.getCommonFilms(userId, friendId);
        log.debug("- getCommonFilms: {}", films);
        return films;
    }

    public Film addFilm(Film film) {
        log.debug("+ addFilm: {}", film);
        Film addedFilm = filmStorage.getFilmById(filmStorage.addFilm(film));
        log.debug("- addFilm: {}", addedFilm);
        return addedFilm;
    }

    public Film updateFilm(Film film) {
        log.debug("+ updateFilm: {}", film);
        Film updatedFilm = filmStorage.getFilmById(filmStorage.updateFilm(film));
        log.debug("- updateFilm: {}", updatedFilm);
        return updatedFilm;
    }

    public void deleteFilm(Long filmId) {
        log.debug("+ deleteFilm: filmId={}", filmId);
        filmStorage.deleteFilm(filmId);
        log.debug("- deleteFilm");
    }

    public void addLike(long filmId, long userId) {
        log.debug("+ addLike: filmId={}, userId={}", filmId, userId);
        filmStorage.addLike(filmId, userId);
        eventStorage.addEvent(UserEventType.LIKE, UserEventOperation.ADD, userId, filmId);
        log.debug("- addLike: likesCount={}", getFilmById(filmId).getLikesCount());
    }

    public void removeLike(long filmId, long userId) {
        log.debug("+ removeLike filmId={}, userId={}", filmId, userId);
        filmStorage.removeLike(filmId, userId);
        eventStorage.addEvent(UserEventType.LIKE, UserEventOperation.REMOVE, userId, filmId);
        log.debug("- removeLike: likesCount={}", getFilmById(filmId).getLikesCount());
    }

    public List<Film> getMostPopularFilms(int count, Long genreId, Integer year) {
        log.debug("+ getMostPopularFilms: {}", count);

        List<Film> popularFilms = filmStorage.getPopularFilms(count, genreId, year);

        log.debug("- getMostPopularFilms: {}", popularFilms);

        return popularFilms;
    }

    public List<Film> getFilmsByDirector(long directorId, String sortBy) {
        log.debug("+ getFilmsByDirector: directorId={}", directorId);
        List<Film> films = filmStorage.getFilmsByDirector(directorId, sortBy);
        log.debug("- getFilmsByDirector: {}", films);
        return films;
    }

    public List<Film> getFilmsBySearchQuery(String query, List<String> by) {
        log.debug("+ getFilmsBySearchQuery: query={}, by={}", query, by);
        List<Film> films = filmStorage.getFilmsBySearchQuery(query, by);
        log.debug("- getFilmsBySearchQuery: {}", films);
        return films;
    }

    public List<Film> getRecommendations(Long userId) {
        List<Film> films = filmStorage.getRecommendations(userId);
        return films;
    }
}