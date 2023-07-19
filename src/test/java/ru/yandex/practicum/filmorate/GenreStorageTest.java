package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.impl.GenreDbStorageImpl;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class GenreStorageTest {
    private final GenreDbStorageImpl genreStorage;

    @Test
    @DirtiesContext
    @DisplayName("Возврат жанра по идентификатору")
    public void shouldReturnGenreById() {
        Genre genre = genreStorage.getGenreById(1L);
        Assertions.assertThat(genre)
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DirtiesContext
    @DisplayName("Возврат всех жанров")
    public void shouldReturnGenres() {
        List<Genre> genres = genreStorage.getGenres();
        Assertions.assertThat(genres)
                .extracting("id")
                .contains(1L, 2L, 3L, 4L, 5L, 6L);
    }
}