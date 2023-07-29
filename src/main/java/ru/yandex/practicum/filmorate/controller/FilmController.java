package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {
    private final FilmService filmService;

    @GetMapping
    public List<Film> getFilms() {
        log.info("+ getFilms");
        List<Film> films = filmService.getFilms();
        log.info("- getFilms: {}", films);
        return films;
    }

    @GetMapping("/{filmId}")
    public Film getFilmById(@PathVariable Long filmId) {
        log.info("+ getFilmById: filmId={}", filmId);
        Film film = filmService.getFilmById(filmId);
        log.info("- getFilmById: {}", film);
        return film;
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(
            @RequestParam(defaultValue = "10") int count,
            @RequestParam(required = false) Long genreId,
            @RequestParam(required = false) Integer year) {
        log.info("+ getMostPopularFilms: {}", count);
        List<Film> popularFilms = filmService.getMostPopularFilms(count, genreId, year);
        log.info("- getMostPopularFilms: {}", popularFilms);
        return popularFilms;
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(@PathVariable long directorId, @RequestParam String sortBy) {
        log.info("+ getFilmsByDirector: directorId={}", directorId);
        List<Film> films = filmService.getFilmsByDirector(directorId, sortBy);
        log.info("- getFilmsByDirector: {}", films);
        return films;
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam int userId, @RequestParam int friendId) {
        log.info("+ getCommonFilms: userId={}, friendId={}", userId, friendId);
        List<Film> films = filmService.getCommonFilms(userId, friendId);
        log.info("- getCommonFilms: {}", films);
        return films;
    }

    @GetMapping("/search")
    public List<Film> getFilmsBySearchQuery(@RequestParam String query, @RequestParam List<String> by) {
        log.info("+ getFilmsBySearchQuery: query={}, by={}", query, by);
        List<Film> films = filmService.getFilmsBySearchQuery(query, by);
        log.info("- getFilmsBySearchQuery: {}", films);
        return films;
    }

    @PostMapping
    public Film addFilm(@Valid @RequestBody Film film) {
        log.info("+ addFilm: {}", film);
        Film addedFilm = filmService.addFilm(film);
        log.info("- addFilm: {}", addedFilm);
        return addedFilm;
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) {
        log.info("+ updateFilm: {}", film);
        Film updatedFilm = filmService.updateFilm(film);
        log.info("- updateFilm: {}", updatedFilm);
        return updatedFilm;
    }

    @DeleteMapping("/{filmId}")
    public void deleteFilm(@PathVariable("filmId") Long filmId) {
        log.info("+ deleteFilm: filmId={}", filmId);
        filmService.deleteFilm(filmId);
        log.info("- deleteFilm");
    }

    @PutMapping("/{filmId}/like/{userId}")
    public void addLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("+ addLike: filmId={}, userId={}", filmId, userId);
        filmService.addLike(filmId, userId);
        log.info("- addLike: likesCount={}", getFilmById(filmId).getLikesCount());
    }

    @DeleteMapping("/{filmId}/like/{userId}")
    public void removeLike(@PathVariable Long filmId, @PathVariable Long userId) {
        log.info("+ removeLike filmId={}, userId={}", filmId, userId);
        filmService.removeLike(filmId, userId);
        log.info("- removeLike: likesCount={}", getFilmById(filmId).getLikesCount());
    }
}