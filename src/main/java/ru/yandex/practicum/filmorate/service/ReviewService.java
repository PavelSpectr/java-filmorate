package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class ReviewService {
    private final ReviewStorage reviewStorage;

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    public ReviewService(ReviewStorage reviewStorage, @Qualifier("filmDbStorage") FilmStorage filmStorage,
                         @Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.reviewStorage = reviewStorage;
    }

    public Review getReviewById(Long reviewId) {
        log.debug("+ getReviewById: reviewId={}", reviewId);
        Review review = reviewStorage.getReviewById(reviewId);
        log.debug("- getReviewById: {}", review);
        return review;
    }

    public Review addReview(Review review) {
        log.debug("+ addReview: {}", review);
        filmStorage.getFilmById(review.getFilmId());
        userStorage.getUserById(review.getUserId());
        Review addedReview = reviewStorage.addReview(review);
        log.debug("- addReview: {}", addedReview);
        return addedReview;
    }

    public Review updateReview(Review review) {
        log.debug("+ updateReview: {}", review);
        Review updatedReview = reviewStorage.updateReview(review);
        log.debug("- addReview: {}", updatedReview);
        return updatedReview;
    }

    public void deleteReview(Long reviewId) {
        log.debug("+ deleteReview: reviewId={}", reviewId);
        reviewStorage.deleteReview(reviewId);
        log.debug("+ deleteReview: reviewId={}", reviewId);
    }

    public List<Review> getReviewsOfFilm(Long filmId, Long count) {
        List<Review> reviews;
        log.debug("+ getReviewsOfFilm: filmId={}, count={}", filmId, count);
        if (filmId == null){
            reviews = reviewStorage.getReviews(count);
        } else {
            reviews = reviewStorage.getReviewsOfFilm(filmId, count);
        }
        log.debug("+ getReviewsOfFilm: ", reviews);
        return reviews;
    }

    public void addLike(Long reviewId, Long userId) {
        log.debug("+ addLike: reviewId = {}, userId = {}", reviewId, userId);
        reviewStorage.addLike(reviewId, userId);
        log.debug("- addLike: reviewId = {}, userId = {}", reviewId, userId);
    }

    public void addDislike(Long reviewId, Long userId) {
        log.debug("+ addDislike: reviewId = {}, userId = {}", reviewId, userId);
        reviewStorage.addDislike(reviewId, userId);
        log.debug("- addDislike: reviewId = {}, userId = {}", reviewId, userId);
    }

    public void removeLike(Long reviewId, Long userId) {
        log.debug("+ removeLike: reviewId = {}, userId = {}", reviewId, userId);
        reviewStorage.removeLike(reviewId, userId);
        log.debug("- removeLike: reviewId = {}, userId = {}", reviewId, userId);
    }

    public void removeDislike(Long reviewId, Long userId) {
        log.debug("+ removeDislike: reviewId = {}, userId = {}", reviewId, userId);
        reviewStorage.removeDislike(reviewId, userId);
        log.debug("- removeDislike: reviewId = {}, userId = {}", reviewId, userId);
    }
}
