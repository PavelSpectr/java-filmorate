package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.service.MpaRatingService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class MpaRatingController {
    private final MpaRatingService mpaRatingService;

    @GetMapping
    public List<MpaRating> getMpaRatings() {
        log.info("+ getMpaRatings");
        List<MpaRating> mpaRatings = mpaRatingService.getMpaRatings();
        log.info("- getMpaRatings: {}", mpaRatings);
        return mpaRatings;
    }

    @GetMapping("/{mpaRatingId}")
    public MpaRating getMpaRatingById(@PathVariable Long mpaRatingId) {
        log.info("+ getMpaRatingById: mpaRatingId={}", mpaRatingId);
        MpaRating mpaRating = mpaRatingService.getMpaRatingById(mpaRatingId);
        log.info("- getMpaRatingById: {}", mpaRating);
        return mpaRating;
    }
}