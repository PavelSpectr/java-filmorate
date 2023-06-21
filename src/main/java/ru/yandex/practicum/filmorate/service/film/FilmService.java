package ru.yandex.practicum.filmorate.service.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;
import java.util.Set;

public interface FilmService {

    Set<Long> addLike(Long filmId, Long userId);

    Set<Long> delLike(Long filmId, Long userId);

    List<Film> getTopRatedFilms(Long count);

    Film createFilm(Film film);

    Film updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(Long filmId);
}
