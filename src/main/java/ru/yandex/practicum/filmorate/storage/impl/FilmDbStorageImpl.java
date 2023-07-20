package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Component("filmDbStorage")
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_FILM_BY_ID_QUERY = "SELECT " +
            "films.id, " +
            "films.name, " +
            "films.description, " +
            "films.release_date, " +
            "films.duration_minutes, " +
            "films.mpa_rating_id, " +
            "mpa.name AS mpa_rating_name, " +
            "GROUP_CONCAT(genres.id || ',' || genres.name separator ';') AS genres, " +
            "COUNT(DISTINCT(film_likes.user_id)) AS likes_count " +
            "FROM films " +
            "LEFT JOIN film_genres ON films.id = film_genres.film_id " +
            "LEFT JOIN genres ON film_genres.genre_id = genres.id " +
            "LEFT JOIN mpa_ratings AS mpa ON films.mpa_rating_id = mpa.id " +
            "LEFT JOIN film_likes ON films.id = film_likes.film_id " +
            "WHERE films.id = ? " +
            "GROUP BY films.id";
    private static final String SELECT_ALL_FILMS_QUERY = "SELECT " +
            "films.id, " +
            "films.name, " +
            "films.description, " +
            "films.release_date, " +
            "films.duration_minutes, " +
            "films.mpa_rating_id, " +
            "mpa.name AS mpa_rating_name, " +
            "GROUP_CONCAT(genres.id || ',' || genres.name separator ';') AS genres, " +
            "COUNT(DISTINCT(film_likes.user_id)) AS likes_count " +
            "FROM films " +
            "LEFT JOIN film_genres ON films.id = film_genres.film_id " +
            "LEFT JOIN genres ON film_genres.genre_id = genres.id " +
            "LEFT JOIN mpa_ratings AS mpa ON films.mpa_rating_id = mpa.id " +
            "LEFT JOIN film_likes ON films.id = film_likes.film_id " +
            "GROUP BY films.id";
    private static final String SELECT_POPULAR_FILMS_QUERY_1 = "SELECT " +
            "films.id, " +
            "films.name, " +
            "films.description, " +
            "films.release_date, " +
            "films.duration_minutes, " +
            "films.mpa_rating_id, " +
            "mpa.name AS mpa_rating_name, " +
            "GROUP_CONCAT(genres.id || ',' || genres.name separator ';') AS genres, " +
            "COUNT(DISTINCT(film_likes.user_id)) AS likes_count " +
            "FROM films ";
    private static final String SELECT_POPULAR_FILMS_QUERY_GENRE =
            "JOIN film_genres AS fg ON films.id = fg.film_id AND fg.genre_id = ? ";
    private static final String SELECT_POPULAR_FILMS_QUERY_2 =
            "LEFT JOIN film_genres AS fgl ON films.id = fgl.film_id " +
                    "LEFT JOIN genres ON fgl.genre_id = genres.id " +
                    "LEFT JOIN mpa_ratings AS mpa ON films.mpa_rating_id = mpa.id " +
                    "LEFT JOIN film_likes ON films.id = film_likes.film_id ";
    private static final String SELECT_POPULAR_FILMS_QUERY_YEAR =
            "WHERE EXTRACT(YEAR FROM films.release_date) = ? ";
    private static final String SELECT_POPULAR_FILMS_QUERY_3 =
            "GROUP BY films.id " +
                    "ORDER BY likes_count DESC " +
                    "LIMIT ? ";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration_minutes = ?, mpa_rating_id = ? WHERE id = ?";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String DELETE_FILM_GENRES_QUERY = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String INSERT_FILM_LIKES_QUERY = "INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)";
    private static final String DELETE_FILM_LIKES_QUERY = "DELETE FROM film_likes WHERE user_id = ? AND film_id = ?";

    private static final String FILM_LIKES_EXIST_QUERY = "SELECT " +
            "film_likes.film_id " +
            "FROM film_likes " +
            "WHERE film_likes.film_id = ? AND film_likes.user_id = ?";

    @Override
    public Film getFilmById(Long filmId) {
        Optional<Film> film = jdbcTemplate.query(SELECT_FILM_BY_ID_QUERY, filmRowMapper(), filmId)
                .stream()
                .findFirst();

        if (film.isEmpty()) {
            log.error("Фильм #" + filmId + " не найден.");
            throw new NotFoundException("Фильм #" + filmId + " не найден.");
        }

        return film.get();
    }

    private boolean filmLikeExist(long filmId, long userId) {
        Optional<Long> filmLikeExist = jdbcTemplate.query(FILM_LIKES_EXIST_QUERY, (rs, rowNum) -> rs.getLong("film_id"), filmId, userId)
                .stream()
                .findFirst();

        return filmLikeExist.isPresent();
    }

    @Override
    public List<Film> getFilms() {
        return jdbcTemplate.query(SELECT_ALL_FILMS_QUERY, filmRowMapper());
    }

    @Override
    public List<Film> getPopularFilms(int count, Long genreId, Integer year) {
        String query = SELECT_POPULAR_FILMS_QUERY_1;
        if (genreId != null) {
            query += SELECT_POPULAR_FILMS_QUERY_GENRE;
        }
        query += SELECT_POPULAR_FILMS_QUERY_2;
        if (year != null) {
            query += SELECT_POPULAR_FILMS_QUERY_YEAR;
        }
        query += SELECT_POPULAR_FILMS_QUERY_3;
        if (genreId != null && year != null) {
            return jdbcTemplate.query(query, filmRowMapper(), genreId, year, count);
        }
        if (genreId != null) {
            return jdbcTemplate.query(query, filmRowMapper(), genreId, count);
        } else if (year != null) {
            return jdbcTemplate.query(query, filmRowMapper(), year, count);
        } else {
            return jdbcTemplate.query(query, filmRowMapper(), count);
        }
    }

    @Override
    public Long addFilm(Film film) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        Map<String, String> params = Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", film.getReleaseDate().toString(),
                "duration_minutes", film.getDuration().toString(),
                "mpa_rating_id", film.getMpa().getId().toString()
        );

        final Long filmId = simpleJdbcInsert.executeAndReturnKey(params).longValue();

        Set<Genre> genres = film.getGenres();
        if (Objects.nonNull(genres)) {
            genres.forEach(genre -> jdbcTemplate.update(
                    INSERT_FILM_GENRES_QUERY,
                    filmId,
                    genre.getId()
            ));
        }

        return filmId;
    }

    @Override
    public Long updateFilm(Film film) {
        final Long filmId = film.getId();

        jdbcTemplate.update(UPDATE_FILM_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), filmId);
        jdbcTemplate.update(DELETE_FILM_GENRES_QUERY, filmId);

        Set<Genre> genres = film.getGenres();
        if (Objects.nonNull(genres)) {
            genres.forEach(genre -> jdbcTemplate.update(
                    INSERT_FILM_GENRES_QUERY,
                    filmId,
                    genre.getId()
            ));
        }

        return filmId;
    }

    @Override
    public void deleteFilm(long filmId) {
        jdbcTemplate.update(DELETE_FILM_QUERY, filmId);
    }

    @Override
    public void addLike(long filmId, long userId) {
        jdbcTemplate.update(INSERT_FILM_LIKES_QUERY, userId, filmId);
    }

    @Override
    public void removeLike(long filmId, long userId) {
        if (!filmLikeExist(filmId, userId)) {
            log.error("Лайк пользователя #" + userId + " не найден.");
            throw new NotFoundException("Лайк пользователя #" + userId + " не найден.");
        }
        jdbcTemplate.update(DELETE_FILM_LIKES_QUERY, userId, filmId);
    }

    private RowMapper<Film> filmRowMapper() {
        return (rs, rowNum) -> {
            Film film = new Film();
            film.setId(rs.getLong("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseDate(rs.getDate("release_date").toLocalDate());
            film.setDuration(rs.getInt("duration_minutes"));

            MpaRating mpaRating = new MpaRating(
                    rs.getLong("mpa_rating_id"),
                    rs.getString("mpa_rating_name")
            );
            film.setMpa(mpaRating);
            film.setGenres(parseGenres(rs.getString("genres")));
            film.setLikesCount(rs.getInt("likes_count"));

            return film;
        };
    }

    private Set<Genre> parseGenres(String genresString) {
        if (Objects.isNull(genresString)) {
            return Collections.emptySet();
        }

        return Arrays.stream(genresString.split(";"))
                .map(genreIdAndName -> {
                    String[] parts = genreIdAndName.split(",");
                    final long genreId = Long.parseLong(parts[0]);
                    String genreName = parts[1];
                    return new Genre(genreId, genreName);
                })
                .collect(Collectors.toSet());
    }

}