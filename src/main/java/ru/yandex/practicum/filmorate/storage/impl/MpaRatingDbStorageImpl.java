package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.MpaRatingStorage;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class MpaRatingDbStorageImpl implements MpaRatingStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_MPA_RATING_BY_ID_QUERY = "SELECT mpa_ratings.id, mpa_ratings.name FROM mpa_ratings WHERE id = ?";
    private static final String SELECT_ALL_MPA_RATINGS_QUERY = "SELECT mpa_ratings.id, mpa_ratings.name FROM mpa_ratings";

    @Override
    public MpaRating getMpaRatingById(Long mpaRatingId) {
        Optional<MpaRating> mpaRatings = jdbcTemplate.query(SELECT_MPA_RATING_BY_ID_QUERY, mpaRatingRowMapper(), mpaRatingId)
                .stream()
                .findFirst();

        if (mpaRatings.isEmpty()) {
            log.error("Рейтинг #" + mpaRatingId + " не найден.");
            throw new NotFoundException("Рейтинг #" + mpaRatingId + " не найден.");
        }

        return mpaRatings.get();
    }

    @Override
    public List<MpaRating> getMpaRatings() {
        return jdbcTemplate.query(SELECT_ALL_MPA_RATINGS_QUERY, mpaRatingRowMapper());
    }

    private RowMapper<MpaRating> mpaRatingRowMapper() {
        return (rs, rowNum) -> new MpaRating(rs.getLong("id"), rs.getString("name"));
    }
}