package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.impl.MpaRatingDbStorageImpl;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class MpaRatingStorageTest {
    private final MpaRatingDbStorageImpl mpaRatingStorage;

    @Test
    @DirtiesContext
    @DisplayName("Возврат МРА рейтинга по идентификатору")
    public void shouldReturnMpaRatingById() {
        MpaRating mpaRating = mpaRatingStorage.getMpaRatingById(1L);
        Assertions.assertThat(mpaRating)
                .hasFieldOrPropertyWithValue("id", 1L);
    }

    @Test
    @DirtiesContext
    @DisplayName("Возврат всех МРА рейтингов")
    public void shouldReturnMpaRatings() {
        List<MpaRating> mpaRatings = mpaRatingStorage.getMpaRatings();
        Assertions.assertThat(mpaRatings)
                .extracting("id")
                .contains(1L, 2L, 3L, 4L, 5L);
    }
}