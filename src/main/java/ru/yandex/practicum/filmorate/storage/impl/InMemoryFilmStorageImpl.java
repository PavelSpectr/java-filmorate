package ru.yandex.practicum.filmorate.storage.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.time.temporal.TemporalField;
import java.time.temporal.TemporalUnit;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryFilmStorageImpl implements FilmStorage {
    private final Map<Long, Film> filmsData = new HashMap<>();

    // Здесь за ключ будет ид фильма, а за значение будет ид пользователя
    private final Set<AbstractMap.SimpleEntry<Long, Long>> likeStore = new HashSet<>();

    @Override
    public Film getFilmById(Long filmId) {
        if (!filmsData.containsKey(filmId)) {
            log.error("Фильм #" + filmId + " не найден.");
            throw new NotFoundException("Фильм #" + filmId + " не найден.");
        }

        return filmsData.get(filmId);
    }

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(filmsData.values());
    }

    @Override
    public List<Film> getPopularFilms(int count, Long genreId, Integer year) {
        return new ArrayList<>(filmsData.values()).stream().filter(film -> {
                    if (genreId != null) {
                        boolean isNoGenre = true;
                        for (Genre genre : film.getGenres()) {
                            if (genre.getId().equals(genreId)) {
                                isNoGenre = false;
                            }
                        }
                        if (isNoGenre) {
                            return false;
                        }
                    }
                    if (year != null) {
                        if (film.getReleaseDate().getYear() == year) {
                            return false;
                        }
                    }
                    return true;
                })
                .sorted(Collections.reverseOrder(Comparator.comparingInt(Film::getLikesCount)))
                .limit(count)
                .collect(Collectors.toList());
    }

    @Override
    public Long addFilm(Film film) {
        final Long id = filmsData.size() + 1L;
        film.setId(id);
        film.setLikesCount(0);
        filmsData.put(id, film);
        return film.getId();
    }

    @Override
    public Long updateFilm(Film updatedFilm) {
        final Long filmId = updatedFilm.getId();

        if (!filmsData.containsKey(filmId)) {
            log.error("Фильм #" + filmId + " не найден.");
            throw new NotFoundException("Фильм #" + filmId + " не найден.");
        }

        Film film = filmsData.get(filmId);

        film.setName(updatedFilm.getName());
        film.setDescription(updatedFilm.getDescription());
        film.setReleaseDate(updatedFilm.getReleaseDate());
        film.setDuration(updatedFilm.getDuration());
        return filmId;
    }

    @Override
    public void deleteFilm(long filmId) {
        removeAllFilmLikes(filmId);
        filmsData.remove(filmId);
    }

    @Override
    public void addLike(long filmId, long userId) {
        getFilmById(filmId).setLikesCount(getFilmById(filmId).getLikesCount() + 1);
        likeStore.add(new AbstractMap.SimpleEntry<>(filmId, userId));
    }

    private void removeAllFilmLikes(Long filmId) {
        likeStore.removeIf(pair -> pair.getKey().equals(filmId));
    }

    @Override
    public void removeLike(long filmId, long userId) {
        if (!likeStore.contains(new AbstractMap.SimpleEntry<>(filmId, userId))) {
            log.error("Лайк пользователя #" + userId + " не найден.");
            throw new NotFoundException("Лайк пользователя #" + userId + " не найден.");
        }
        getFilmById(filmId).setLikesCount(getFilmById(filmId).getLikesCount() - 1);
        likeStore.remove(new AbstractMap.SimpleEntry<>(filmId, userId));
    }

}
