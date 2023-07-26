package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.UserEventOperation;
import ru.yandex.practicum.filmorate.model.UserEventType;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserEventStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@Slf4j
public class ReviewService {
    private final ReviewStorage reviewStorage;

    private final FilmStorage filmStorage;

    private final UserStorage userStorage;

    private final UserEventStorage eventStorage;

    public ReviewService(ReviewStorage reviewStorage,
                         @Qualifier("filmDbStorage") FilmStorage filmStorage,
                         @Qualifier("userDbStorage") UserStorage userStorage,
                         @Qualifier("userEventDbStorage") UserEventStorage eventStorage) {
        this.userStorage = userStorage;
        this.filmStorage = filmStorage;
        this.reviewStorage = reviewStorage;
        this.eventStorage = eventStorage;
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
        eventStorage.addEvent(UserEventType.REVIEW, UserEventOperation.ADD, review.getUserId(), addedReview.getReviewId());
        log.debug("- addReview: {}", addedReview);
        return addedReview;
    }

    public Review updateReview(Review review) {
        log.debug("+ updateReview: {}", review);
        Review updatedReview = reviewStorage.updateReview(review);
        eventStorage.addEvent(UserEventType.REVIEW, UserEventOperation.UPDATE, updatedReview.getUserId(), updatedReview.getReviewId());
        log.debug("- addReview: {}", updatedReview);
        return updatedReview;
    }

    public void deleteReview(Long reviewId) {
        log.debug("+ deleteReview: reviewId={}", reviewId);
        Long userId = reviewStorage.getReviewById(reviewId).getUserId();
        reviewStorage.deleteReview(reviewId);
        eventStorage.addEvent(UserEventType.REVIEW, UserEventOperation.REMOVE, userId, reviewId);
        log.debug("+ deleteReview: reviewId={}", reviewId);
    }

    public List<Review> getReviewsOfFilm(Long filmId, Long count) {
        List<Review> reviews;
        log.debug("+ getReviewsOfFilm: filmId={}, count={}", filmId, count);
        if (filmId == null) {
            reviews = reviewStorage.getReviews(count);
        } else {
            reviews = reviewStorage.getReviewsOfFilm(filmId, count);
        }
        log.debug("+ getReviewsOfFilm: {}", reviews);
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
