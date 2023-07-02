package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;

    @Override
    public Film createFilm(Film film) {
        log.debug("Создание нового фильма:");
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        log.debug("Обновление данных фильма:");
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        log.debug("Получение списка всех фильмов:");
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilmById(Long filmId) {
        log.debug("Получение фильма по id:");
        return filmStorage.getFilmById(filmId);
    }


    @Override
    public Set<Long> addLike(Long filmId, Long userId) {
        Set<Long> likes = filmStorage.getFilmById(filmId).getLikes();
        if (likes.contains(userId)) {
            log.debug("Пользователь с id {} уже поставил лайк этому фильму с id={}", userId, filmId);
            return likes;
        }
        likes.add(userId);
        log.debug("Пользователь с id {} успешно поставил лайк этому фильму с id={}", userId, filmId);
        return likes;
    }

    @Override
    public Set<Long> delLike(Long filmId, Long userId) {
        Set<Long> likes = filmStorage.getFilmById(filmId).getLikes();
        if (!likes.contains(userId)) {
            log.debug("Пользователь с id {} не ставил или уже удалил лайк этому фильму", userId);
            throw new ValidationException("Не найден пользователь с id=" + userId);
        }
        likes.remove(userId);
        log.debug("Пользователь с id {} успешно удалил лайк этому фильму", userId);
        return likes;
    }

    @Override
    public List<Film> getTopRatedFilms(Long count) {
        log.debug("Выведен список Топ-{} фильмов", count);
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
    }
}
