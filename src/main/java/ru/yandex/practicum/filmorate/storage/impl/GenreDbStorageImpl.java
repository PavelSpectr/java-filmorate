package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenreDbStorageImpl implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_GENRE_BY_ID_QUERY = "SELECT genres.id, genres.name FROM genres WHERE id = ?";
    private static final String SELECT_ALL_GENRES_QUERY = "SELECT genres.id, genres.name FROM genres";

    @Override
    public Genre getGenreById(Long genreId) {
        Optional<Genre> genre = jdbcTemplate.query(SELECT_GENRE_BY_ID_QUERY, genreRowMapper(), genreId)
                .stream()
                .findFirst();

        if (genre.isEmpty()) {
            log.error("Жанр #" + genreId + " не найден.");
            throw new NotFoundException("Жанр #" + genreId + " не найден.");
        }

        return genre.get();
    }

    @Override
    public List<Genre> getGenres() {
        return jdbcTemplate.query(SELECT_ALL_GENRES_QUERY, genreRowMapper());
    }

    private RowMapper<Genre> genreRowMapper() {
        return (rs, rowNum) -> new Genre(rs.getLong("id"), rs.getString("name"));
    }
}