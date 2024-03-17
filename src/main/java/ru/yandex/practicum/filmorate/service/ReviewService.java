package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.model.UserEventOperation;
import ru.yandex.practicum.filmorate.model.UserEventType;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.UserEventStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewStorage reviewStorage;
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final UserEventStorage eventStorage;

    public Review getReviewById(Long reviewId) {
        return reviewStorage.getReviewById(reviewId);
    }

    public Review addReview(Review review) {
        filmStorage.getFilmById(review.getFilmId());
        userStorage.getUserById(review.getUserId());
        Review addedReview = reviewStorage.addReview(review);
        UserEvent userEvent = new UserEvent(UserEventType.REVIEW,
                UserEventOperation.ADD,
                review.getUserId(),
                addedReview.getReviewId());

        eventStorage.addEvent(userEvent);

        return addedReview;
    }

    public Review updateReview(Review review) {
        Review updatedReview = reviewStorage.updateReview(review);
        UserEvent userEvent = new UserEvent(UserEventType.REVIEW,
                UserEventOperation.UPDATE,
                updatedReview.getUserId(),
                updatedReview.getReviewId());

        eventStorage.addEvent(userEvent);

        return updatedReview;
    }

    public void deleteReview(Long reviewId) {
        Long userId = reviewStorage.getReviewById(reviewId).getUserId();
        reviewStorage.deleteReview(reviewId);
        UserEvent userEvent = new UserEvent(UserEventType.REVIEW, UserEventOperation.REMOVE, userId, reviewId);
        eventStorage.addEvent(userEvent);
    }

    public List<Review> getReviewsOfFilm(Long filmId, Long count) {
        List<Review> reviews;
        if (filmId == null) {
            reviews = reviewStorage.getReviews(count);
        } else {
            reviews = reviewStorage.getReviewsOfFilm(filmId, count);
        }
        return reviews;
    }

    public void addLike(Long reviewId, Long userId) {
        reviewStorage.addLike(reviewId, userId);
    }

    public void addDislike(Long reviewId, Long userId) {
        reviewStorage.addDislike(reviewId, userId);
    }

    public void removeLike(Long reviewId, Long userId) {
        reviewStorage.removeLike(reviewId, userId);
    }

    public void removeDislike(Long reviewId, Long userId) {
        reviewStorage.removeDislike(reviewId, userId);
    }
}
