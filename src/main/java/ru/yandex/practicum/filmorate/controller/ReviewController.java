package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Slf4j
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/{reviewId}")
    public Review getReviewById(@PathVariable Long reviewId) {
        log.info("+ getReviewById: reviewId={}", reviewId);
        Review review = reviewService.getReviewById(reviewId);
        log.info("- getReviewById: {}", review);
        return review;
    }

    @PostMapping
    public Review addReview(@Valid @RequestBody Review review) {
        log.info("+ addReview: {}", review);
        Review addedReview = reviewService.addReview(review);
        log.info("- addReview: {}", addedReview);
        return addedReview;
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        log.info("+ updateReview: {}", review);
        Review updatedReview = reviewService.updateReview(review);
        log.info("- addReview: {}", updatedReview);
        return updatedReview;
    }

    @DeleteMapping("/{reviewId}")
    public void deleteReview(@PathVariable Long reviewId) {
        log.info("+ deleteReview: reviewId={}", reviewId);
        reviewService.deleteReview(reviewId);
        log.info("+ deleteReview");
    }

    @GetMapping
    public List<Review> getReviews(@RequestParam(defaultValue = "", required = false) Long filmId,
                                   @RequestParam(defaultValue = "10") Long count) {
        log.info("+ getReviews: filmId={}, count={}", filmId, count);
        List<Review> reviews = reviewService.getReviewsOfFilm(filmId, count);
        log.info("+ getReviews: {}", reviews);
        return reviews;
    }

    @PutMapping("/{reviewId}/like/{userId}")
    public void addLike(@PathVariable Long reviewId, @PathVariable Long userId) {
        log.info("+ addLike: reviewId = {}, userId = {}", reviewId, userId);
        reviewService.addLike(reviewId, userId);
        log.info("- addLike");
    }

    @PutMapping("/{reviewId}/dislike/{userId}")
    public void addDislike(@PathVariable Long reviewId, @PathVariable Long userId) {
        log.info("+ addDislike: reviewId = {}, userId = {}", reviewId, userId);
        reviewService.addDislike(reviewId, userId);
        log.info("- addDislike");
    }

    @DeleteMapping("/{reviewId}/like/{userId}")
    public void removeLike(Long reviewId, Long userId) {
        log.info("+ removeLike: reviewId = {}, userId = {}", reviewId, userId);
        reviewService.removeLike(reviewId, userId);
        log.info("- removeLike");
    }

    @DeleteMapping("/{reviewId}/dislike/{userId}")
    public void removeDislike(Long reviewId, Long userId) {
        log.info("+ removeDislike: reviewId = {}, userId = {}", reviewId, userId);
        reviewService.removeDislike(reviewId, userId);
        log.info("- removeDislike");
    }
}
