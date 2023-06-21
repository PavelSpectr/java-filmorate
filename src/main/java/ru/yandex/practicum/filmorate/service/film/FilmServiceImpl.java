package ru.yandex.practicum.filmorate.service.film;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
public class FilmServiceImpl implements FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Override
    public Film createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    @Override
    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    @Override
    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    @Override
    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }


    @Override
    public Set<Long> addLike(Long filmId, Long userId) {
        Set<Long> likes = filmStorage.getFilmById(filmId).getLikes();
        if (likes.contains(userId)) {
            log.debug("Пользователь с id {} уже поставил лайк этому фильму с id={}", userId, filmId);
        } else {
            likes.add(userId);
            log.debug("Пользователь с id {} успешно поставил лайк этому фильму с id={}", userId, filmId);
        }
        return likes;
    }

    @Override
    public Set<Long> delLike(Long filmId, Long userId) {
        Set<Long> likes = filmStorage.getFilmById(filmId).getLikes();
        if (!likes.contains(userId)) {
            log.debug("Пользователь с id {} не ставил или уже удалил лайк этому фильму", userId);
            throw new ValidationException("Не найден пользователь с id=" + userId);
        } else {
            likes.remove(userId);
            log.debug("Пользователь с id {} успешно удалил лайк этому фильму", userId);
        }
        return likes;
    }

    @Override
    public List<Film> getTopRatedFilms(Long count) {
        log.debug("Выведен список Топ-{} фильмов", count);
        return filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
        /*List<Film> topFilms = filmStorage.getAllFilms().stream()
                .sorted((f1, f2) -> Integer.compare(f2.getLikes().size(), f1.getLikes().size()))
                .limit(count)
                .collect(Collectors.toList());
        log.debug("Выведен список Топ-{} фильмов", count);
        return topFilms;*/
    }
}
