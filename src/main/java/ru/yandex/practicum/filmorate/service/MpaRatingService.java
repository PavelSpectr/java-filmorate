package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaRatingService {
    private final MpaRatingStorage mpaRatingStorage;

    public MpaRating getMpaRatingById(Long mpaRatingId) {
        return mpaRatingStorage.getMpaRatingById(mpaRatingId);
    }

    public List<MpaRating> getMpaRatings() {
        return mpaRatingStorage.getMpaRatings();
    }
}