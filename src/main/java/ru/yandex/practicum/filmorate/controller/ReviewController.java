package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{reviewId}")
    public Review getReviewById(@PathVariable Long reviewId) {
        return reviewService.getReviewById(reviewId);
    }

    @PostMapping
    public Review addReview(@Valid @RequestBody Review review) {
        return reviewService.addReview(review);
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        return reviewService.updateReview(review);
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
    }

    @GetMapping
    public List<Review> getReviewsOfFilm(@RequestParam(defaultValue = "") Long filmId,
                                         @RequestParam(defaultValue = "10") Long count) {
        return reviewService.getReviewsOfFilm(filmId, count);
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.addLike(reviewId, userId);
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislike(@PathVariable Long reviewId, @PathVariable Long userId) {
        reviewService.addDislike(reviewId, , userId);
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeLike(Long reviewId, Long userId) {
        reviewService.removeLike(reviewId, , userId);
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeDislike(Long reviewId, Long userId) {
        reviewService.removeDislike(reviewId, userId);
    }
}
