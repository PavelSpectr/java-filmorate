package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.impl.DirectorDbStorageImpl;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class DirectorStorageTest {
    private final DirectorDbStorageImpl directorStorage;

    @Test
    @DirtiesContext
    @DisplayName("Добавление режиссёра и возврат режиссёра по идентификатору")
    public void shouldAddDirectorAndReturnDirectorById() {
        directorStorage.addDirector(new Director("Режиссёр1"));

        Director director = directorStorage.getDirectorById(1);
        Assertions.assertThat(director)
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Режиссёр1");

    }

    @Test
    @DirtiesContext
    @DisplayName("Возврат всех режиссёров")
    public void shouldReturnDirectors() {
        directorStorage.addDirector(new Director("Режиссёр1"));
        directorStorage.addDirector(new Director("Режиссёр2"));

        List<Director> directors = directorStorage.getDirectors();
        Assertions.assertThat(directors)
                .extracting("id")
                .contains(1, 2);
        Assertions.assertThat(directors)
                .extracting("name")
                .contains("Режиссёр1", "Режиссёр2");
    }

    @Test
    @DirtiesContext
    @DisplayName("Обновление режиссёра")
    public void shouldUpdateDirector() {
        directorStorage.addDirector(new Director("Режиссёр1"));
        directorStorage.updateDirector(new Director(1,"Новый Режиссёр"));

        Assertions.assertThat(directorStorage.getDirectorById(1))
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Новый Режиссёр");
    }

    @Test
    @DirtiesContext
    @DisplayName("Удаление режиссёра")
    public void shouldDeleteDirector() {
        directorStorage.addDirector(new Director("Режиссёр1"));
        Assertions.assertThat(directorStorage.getDirectorById(1))
                .hasFieldOrPropertyWithValue("id", 1)
                .hasFieldOrPropertyWithValue("name", "Режиссёр1");

        directorStorage.deleteDirector(1);
        final NotFoundException exception = org.junit.jupiter.api.Assertions.assertThrows(
                NotFoundException.class,
                () -> directorStorage.getDirectorById(1)
        );
        org.junit.jupiter.api.Assertions.assertEquals("Режиссёр #1 не найден.", exception.getMessage());
    }
}
