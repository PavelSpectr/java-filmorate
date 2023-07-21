package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    public Review getReviewById(Long reviewId);

    public Review addReview(Review review);

    public Review updateReview(Review review);

    public void deleteReview(Long reviewId);

    public List<Review> getReviewsOfFilm(Long filmId, Long count);


    public void addLike(Long review_id, Long user_id);


    public void addDislike(Long review_id, Long user_id);


    public void removeLike(Long review_id, Long user_id);


    public void removeDislike(Long review_id, Long user_id);
}

