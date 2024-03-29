package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.ReviewStorage;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class ReviewDbStorageImpl implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    private static final String SELECT_REVIEW_BY_ID_QUERY = "SELECT * FROM reviews WHERE review_id = ?";

    private static final String UPDATE_REVIEW_QUERY = "UPDATE reviews SET content = ?, is_positive = ? " +
            " WHERE review_id = ?";

    private static final String DELETE_REVIEW_QUERY = "DELETE FROM reviews WHERE review_id = ?";

    private static final String SELECT_REVIEWS_OF_FILM_QUERY = "SELECT * " +
            "FROM reviews " +
            "WHERE film_id = ? " +
            "ORDER BY useful DESC " +
            "LIMIT ?";

    private static final String SELECT_ALL_REVIEWS_QUERY = "SELECT * " +
            "FROM reviews " +
            "ORDER BY useful DESC " +
            "LIMIT ?";

    private static final String UPDATE_REVIEW_USEFUL_PLUS_QUERY = "UPDATE reviews SET useful = useful + 1" +
            " WHERE review_id = ?";

    private static final String UPDATE_REVIEW_USEFUL_MINUS_QUERY = "UPDATE reviews SET useful = useful - 1" +
            " WHERE review_id = ?";

    private static final String UPDATE_REVIEW_LIKES_QUERY = "INSERT INTO review_likes VALUES(?, ?, ?)";

    private static final String DELETE_REVIEW_LIKES_QUERY = "DELETE FROM review_likes WHERE review_id = ? AND " +
            "user_id = ? AND is_positive = ?";

    @Override
    public Review getReviewById(Long reviewId) {
        Optional<Review> review = jdbcTemplate.query(SELECT_REVIEW_BY_ID_QUERY, reviewRowMapper(), reviewId)
                .stream()
                .findFirst();

        if (review.isEmpty()) {
            log.error("Отзыв #" + reviewId + " не найден.");
            throw new NotFoundException("Отзыв #" + reviewId + " не найден.");
        }

        return review.get();
    }

    @Override
    public Review addReview(Review review) {
        try {
            SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
            simpleJdbcInsert.withTableName("reviews").usingGeneratedKeyColumns("review_id");
            SqlParameterSource params = new MapSqlParameterSource()
                    .addValue("content", review.getContent())
                    .addValue("is_positive", review.getIsPositive())
                    .addValue("user_id", review.getUserId())
                    .addValue("film_id", review.getFilmId())
                    .addValue("useful", 0);
            Long reviewId = simpleJdbcInsert.executeAndReturnKey(params).longValue();
            review.setReviewId(reviewId);
            return review;
        } catch (DataIntegrityViolationException e) {
            throw new NotFoundException("Пользователь #" + review.getUserId() + " или фильм #" + review.getFilmId() +
                    "не существует.");
        }
    }

    @Override
    public Review updateReview(Review review) {
        jdbcTemplate.update(UPDATE_REVIEW_QUERY, review.getContent(), review.getIsPositive(), review.getReviewId());
        return getReviewById(review.getReviewId());
    }

    @Override
    public void deleteReview(Long reviewId) {
        getReviewById(reviewId);
        jdbcTemplate.update(DELETE_REVIEW_QUERY, reviewId);
    }

    @Override
    public List<Review> getReviewsOfFilm(Long filmId, Long count) {
        return jdbcTemplate.query(SELECT_REVIEWS_OF_FILM_QUERY, reviewRowMapper(), filmId, count);
    }

    @Override
    public List<Review> getReviews(Long count) {
        return jdbcTemplate.query(SELECT_ALL_REVIEWS_QUERY, reviewRowMapper(), count);
    }

    @Override
    public void addLike(Long reviewId, Long userId) {
        jdbcTemplate.update(UPDATE_REVIEW_LIKES_QUERY, reviewId, userId, true);
        jdbcTemplate.update(UPDATE_REVIEW_USEFUL_PLUS_QUERY, reviewId);
    }

    @Override
    public void addDislike(Long reviewId, Long userId) {
        jdbcTemplate.update(UPDATE_REVIEW_LIKES_QUERY, reviewId, userId, false);
        jdbcTemplate.update(UPDATE_REVIEW_USEFUL_MINUS_QUERY, reviewId);
    }

    @Override
    public void removeLike(Long reviewId, Long userId) {
        jdbcTemplate.update(DELETE_REVIEW_LIKES_QUERY, reviewId, userId, true);
        jdbcTemplate.update(UPDATE_REVIEW_USEFUL_MINUS_QUERY, reviewId);
    }

    @Override
    public void removeDislike(Long reviewId, Long userId) {
        jdbcTemplate.update(DELETE_REVIEW_LIKES_QUERY, reviewId, userId, false);
        jdbcTemplate.update(UPDATE_REVIEW_USEFUL_PLUS_QUERY, reviewId);
    }

    private RowMapper<Review> reviewRowMapper() {
        return (rs, rowNum) -> new Review(rs.getLong("review_id"),
                rs.getString("content"),
                rs.getBoolean("is_positive"),
                rs.getLong("user_id"),
                rs.getLong("film_id"),
                rs.getLong("useful"));
    }

}
