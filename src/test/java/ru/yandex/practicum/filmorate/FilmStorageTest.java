package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.impl.FilmDbStorageImpl;
import ru.yandex.practicum.filmorate.storage.impl.UserDbStorageImpl;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class FilmStorageTest {
    private final FilmDbStorageImpl filmStorage;
    private final UserDbStorageImpl userStorage;

    @Test
    @DirtiesContext
    @DisplayName("Возврат фильма по идентификатору")
    public void shouldReturnFilmById() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        film.setMpa(new MpaRating(1L, "G"));

        Long addedFilmId = filmStorage.addFilm(film);
        Film retrievedFilm = filmStorage.getFilmById(addedFilmId);

        Assertions.assertNotNull(retrievedFilm);
        Assertions.assertEquals("Film Name", retrievedFilm.getName());
        Assertions.assertEquals("Film Description", retrievedFilm.getDescription());
        Assertions.assertEquals(LocalDate.now(), retrievedFilm.getReleaseDate());
        Assertions.assertEquals(120, retrievedFilm.getDuration());
        Assertions.assertEquals(new MpaRating(1L, "G"), retrievedFilm.getMpa());
    }

    @Test
    @DirtiesContext
    @DisplayName("Добавление фильма")
    public void shouldAddAndReturnFilm() {
        Film film1 = new Film();
        film1.setName("Film 1");
        film1.setDescription("Film 1 Description");
        film1.setReleaseDate(LocalDate.now());
        film1.setDuration(120);
        film1.setMpa(new MpaRating(1L, "G"));

        Film film2 = new Film();
        film2.setName("Film 2");
        film2.setDescription("Film 2 Description");
        film2.setReleaseDate(LocalDate.now());
        film2.setDuration(90);
        film2.setMpa(new MpaRating(2L, "PG"));

        filmStorage.addFilm(film1);
        filmStorage.addFilm(film2);

        List<Film> films = filmStorage.getFilms();

        Assertions.assertNotNull(films);
        Assertions.assertFalse(films.isEmpty());
        Assertions.assertEquals(2, films.size());

        Film retrievedFilm1 = films.get(0);
        Assertions.assertEquals("Film 1", retrievedFilm1.getName());
        Assertions.assertEquals("Film 1 Description", retrievedFilm1.getDescription());
        Assertions.assertEquals(LocalDate.now(), retrievedFilm1.getReleaseDate());
        Assertions.assertEquals(120, retrievedFilm1.getDuration());
        Assertions.assertEquals(new MpaRating(1L, "G"), retrievedFilm1.getMpa());

        Film retrievedFilm2 = films.get(1);
        Assertions.assertEquals("Film 2", retrievedFilm2.getName());
        Assertions.assertEquals("Film 2 Description", retrievedFilm2.getDescription());
        Assertions.assertEquals(LocalDate.now(), retrievedFilm2.getReleaseDate());
        Assertions.assertEquals(90, retrievedFilm2.getDuration());
        Assertions.assertEquals(new MpaRating(2L, "PG"), retrievedFilm2.getMpa());
    }

    @Test
    @DirtiesContext
    @DisplayName("Обновление фильма")
    public void shouldUpdateFilm() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        film.setMpa(new MpaRating(1L, "G"));

        Long addedFilmId = filmStorage.addFilm(film);
        Film addedFilm = filmStorage.getFilmById(addedFilmId);

        addedFilm.setName("Updated Film Name");
        addedFilm.setDescription("Updated Film Description");
        addedFilm.setReleaseDate(LocalDate.now().plusDays(1));
        addedFilm.setDuration(90);
        addedFilm.setMpa(new MpaRating(2L, "PG"));

        Film updatedFilm = filmStorage.getFilmById(filmStorage.updateFilm(addedFilm));

        Assertions.assertNotNull(updatedFilm);
        Assertions.assertEquals("Updated Film Name", updatedFilm.getName());
        Assertions.assertEquals("Updated Film Description", updatedFilm.getDescription());
        Assertions.assertEquals(LocalDate.now().plusDays(1), updatedFilm.getReleaseDate());
        Assertions.assertEquals(90, updatedFilm.getDuration());
        Assertions.assertEquals(new MpaRating(2L, "PG"), updatedFilm.getMpa());
    }

    @Test
    @DirtiesContext
    @DisplayName("Удаление фильма")
    public void shouldDeleteFilm() {
        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        film.setMpa(new MpaRating(1L, "G"));

        Long addedFilmId = filmStorage.addFilm(film);

        filmStorage.deleteFilm(addedFilmId);

        final NotFoundException exception = Assertions.assertThrows(
                NotFoundException.class,
                () -> filmStorage.getFilmById(addedFilmId)
        );
        Assertions.assertEquals("Фильм #1 не найден.", exception.getMessage());

    }

    @Test
    @DirtiesContext
    @DisplayName("Добавление лайка")
    public void shouldAddLike() {
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setLogin("userLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        final long userId = userStorage.createUser(user);

        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        film.setMpa(new MpaRating(1L, "G"));
        long filmId = filmStorage.addFilm(film);

        filmStorage.addLike(filmId, userId);

        Film retrievedFilm = filmStorage.getFilmById(filmId);

        Assertions.assertNotNull(retrievedFilm);
        Assertions.assertTrue(retrievedFilm.getLikes().contains(userId));
    }

    @Test
    @DirtiesContext
    @DisplayName("Удаление лайка")
    public void shouldRemoveLike() {
        User user = new User();
        user.setEmail("user@mail.ru");
        user.setLogin("userLogin");
        user.setBirthday(LocalDate.of(1990, 1, 1));
        long userId = userStorage.createUser(user);

        Film film = new Film();
        film.setName("Film Name");
        film.setDescription("Film Description");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(120);
        film.setMpa(new MpaRating(1L, "G"));
        long filmId = filmStorage.addFilm(film);

        filmStorage.addLike(filmId, userId);
        filmStorage.removeLike(filmId, userId);

        Film retrievedFilm = filmStorage.getFilmById(filmId);

        Assertions.assertNotNull(retrievedFilm);
        Assertions.assertFalse(retrievedFilm.getLikes().contains(userId));
    }
}