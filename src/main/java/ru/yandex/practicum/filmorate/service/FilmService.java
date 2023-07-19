package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FilmService {

    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
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
        log.debug("- addLike: likes={}", getFilmById(filmId).getLikes());
    }

    public void removeLike(long filmId, long userId) {
        log.debug("+ removeLike filmId={}, userId={}", filmId, userId);
        filmStorage.removeLike(filmId, userId);
        log.debug("- removeLike: likes={}", getFilmById(filmId).getLikes());
    }

    public List<Film> getMostPopularFilms(int count) {
        log.debug("+ getMostPopularFilms: {}", count);

        List<Film> popularFilms = getFilms().stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Film::getLikesCount)))
                .limit(count)
                .collect(Collectors.toList());

        log.debug("- getMostPopularFilms: {}", popularFilms);

        return popularFilms;
    }
}