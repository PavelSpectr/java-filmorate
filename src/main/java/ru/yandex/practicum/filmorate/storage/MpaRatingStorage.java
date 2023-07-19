package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.MpaRating;

import java.util.List;

public interface MpaRatingStorage {
    MpaRating getMpaRatingById(Long mpaRatingId);

    List<MpaRating> getMpaRatings();
}