package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MpaRatingService {
    private final MpaRatingStorage mpaRatingStorage;

    public MpaRating getMpaRatingById(Long mpaRatingId) {
        log.debug("+ getMpaRatingById: mpaRatingId={}", mpaRatingId);
        MpaRating mpaRating = mpaRatingStorage.getMpaRatingById(mpaRatingId);
        log.debug("- getMpaRatingById: {}", mpaRating);
        return mpaRating;
    }

    public List<MpaRating> getMpaRatings() {
        log.debug("+ getMpaRatings");
        List<MpaRating> mpaRatings = mpaRatingStorage.getMpaRatings();
        log.debug("- getMpaRatings: {}", mpaRatings);
        return mpaRatings;
    }
}