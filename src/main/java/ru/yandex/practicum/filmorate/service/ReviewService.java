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
    @Qualifier("userDbStorage")
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
        log.debug("+ getReviewsOfFilm: filmId={}, count={}", filmId, count);
        List<Review> reviewsOfFilm = reviewStorage.getReviewsOfFilm(filmId, count);
        log.debug("+ getReviewsOfFilm: ", reviewsOfFilm);
        return reviewsOfFilm;
    }

    public void addLike(Long review_id, Long user_id) {
        log.debug("+  addLike: reviewId = {}, userId = {}", review_id, user_id);
        reviewStorage.addLike(review_id, user_id);
        log.debug("- addLike: reviewId = {}, userId = {}", review_id, user_id);
    }

    public void addDislike(Long review_id, Long user_id) {
        log.debug("+ addDislike: reviewId = {}, userId = {}", review_id, user_id);
        reviewStorage.addDislike(review_id, user_id);
        log.debug("- addDislike: reviewId = {}, userId = {}", review_id, user_id);
    }

    public void removeLike(Long review_id, Long user_id) {
        log.debug("+ removeLike: reviewId = {}, userId = {}", review_id, user_id);
        reviewStorage.removeLike(review_id, user_id);
        log.debug("- removeLike: reviewId = {}, userId = {}", review_id, user_id);
    }

    public void removeDislike(Long review_id, Long user_id) {
        log.debug("+ removeDislike: reviewId = {}, userId = {}", review_id, user_id);
        reviewStorage.removeDislike(review_id, user_id);
        log.debug("- removeDislike: reviewId = {}, userId = {}", review_id, user_id);
    }
}
