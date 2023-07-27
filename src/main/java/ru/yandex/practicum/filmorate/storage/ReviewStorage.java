package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review getReviewById(Long reviewId);

    Review addReview(Review review);

    Review updateReview(Review review);

    void deleteReview(Long reviewId);

    List<Review> getReviewsOfFilm(Long filmId, Long count);

    List<Review> getReviews(Long count);


    void addLike(Long reviewId, Long userId);


    void addDislike(Long reviewId, Long userId);


    void removeLike(Long reviewId, Long userId);


    void removeDislike(Long reviewId, Long userId);
}

