package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class FilmDbStorageImpl implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String BASE_QUERY =
            "SELECT f.*, " +
                    "mpa.name AS mpa_rating_name, " +
                    "GROUP_CONCAT(genres.id || ',' || genres.name separator ';') AS genres, " +
                    "GROUP_CONCAT(directors.id || ',' || directors.name separator ';') AS directors, " +
                    "COUNT(DISTINCT(fl.user_id)) AS likes_count " +
                    "FROM films AS f " +
                    "LEFT JOIN film_genres AS fg2 ON f.id = fg2.film_id " +
                    "LEFT JOIN genres ON fg2.genre_id = genres.id " +
                    "LEFT JOIN mpa_ratings AS mpa ON f.mpa_rating_id = mpa.id " +
                    "LEFT JOIN film_likes AS fl ON f.id = fl.film_id " +
                    "LEFT JOIN film_directors AS fd ON f.id = fd.film_id " +
                    "LEFT JOIN directors ON fd.director_id = directors.id ";
    private static final String SELECT_FILMS_BY_USER_ID_LIKE =
            "JOIN film_likes AS ufl ON f.id = ufl.film_id AND ufl.user_id = ? " +
                    "GROUP BY f.id " +
                    "ORDER BY likes_count DESC";
    private static final String SELECT_FILM_BY_ID_QUERY = "WHERE f.id = ? GROUP BY f.id";
    private static final String SELECT_ALL_FILMS_QUERY = "GROUP BY f.id";
    private static final String SELECT_FILM_BY_DIRECTOR_SORT_BY_LIKES_QUERY =
            "WHERE fd.director_id = ? " +
                    "GROUP BY f.id " +
                    "ORDER BY likes_count DESC";
    private static final String SELECT_FILM_BY_DIRECTOR_SORT_BY_YEAR_QUERY =
            "WHERE fd.director_id = ? " +
                    "GROUP BY f.id " +
                    "ORDER BY release_date";
    private static final String SELECT_FILM_BY_SEARCH_QUERY_BY_TITLE =
            "WHERE LOWER(f.name) LIKE LOWER(?) GROUP BY f.id ORDER BY likes_count DESC";
    private static final String SELECT_FILM_BY_SEARCH_QUERY_BY_DIRECTOR =
            "WHERE LOWER(directors.name) LIKE LOWER(?) GROUP BY f.id ORDER BY likes_count DESC";
    private static final String SELECT_FILM_BY_SEARCH_QUERY_BY_TITLE_AND_DIRECTOR =
            "WHERE LOWER(f.name) LIKE LOWER(?) " +
                    "OR LOWER(directors.name) LIKE LOWER(?) " +
                    "GROUP BY f.id " +
                    "ORDER BY likes_count DESC";
    private static final String SELECT_POPULAR_FILMS_QUERY_GENRE_FILTER =
            "JOIN film_genres AS fg ON f.id = fg.film_id AND fg.genre_id = ? ";
    private static final String SELECT_POPULAR_FILMS_QUERY_YEAR_FILTER =
            "WHERE EXTRACT(YEAR FROM f.release_date) = ? ";
    private static final String SELECT_POPULAR_FILMS_QUERY_LIMIT =
            "GROUP BY f.id " +
                    "ORDER BY likes_count DESC " +
                    "LIMIT ? ";
    private static final String SELECT_COMMON_FILMS_BY_USER_ID_AND_FRIEND_ID_QUERY =
            "INNER JOIN film_likes AS user_likes ON f.id = user_likes.film_id AND user_likes.user_id = ? " +
                    "INNER JOIN film_likes AS friend_likes ON f.id = friend_likes.film_id AND friend_likes.user_id = ? " +
                    "GROUP BY f.id " +
                    "ORDER BY likes_count DESC";
    private static final String UPDATE_FILM_QUERY = "UPDATE films SET name = ?, description = ?, release_date = ?, duration_minutes = ?, mpa_rating_id = ? WHERE id = ?";
    private static final String INSERT_FILM_GENRES_QUERY = "INSERT INTO film_genres (film_id, genre_id) VALUES (?, ?)";
    private static final String INSERT_FILM_DIRECTORS_QUERY = "INSERT INTO film_directors (film_id, director_id) VALUES (?, ?)";
    private static final String DELETE_FILM_GENRES_QUERY = "DELETE FROM film_genres WHERE film_id = ?";
    private static final String DELETE_FILM_DIRECTORS_QUERY = "DELETE FROM film_directors WHERE film_id = ?";
    private static final String DELETE_FILM_QUERY = "DELETE FROM films WHERE id = ?";
    private static final String INSERT_FILM_LIKES_QUERY = "INSERT INTO film_likes (user_id, film_id) VALUES (?, ?)";
    private static final String DELETE_FILM_LIKES_QUERY = "DELETE FROM film_likes WHERE user_id = ? AND film_id = ?";
    private static final String FILM_LIKES_EXIST_QUERY = "SELECT " +
            "film_likes.film_id " +
            "FROM film_likes " +
            "WHERE film_likes.film_id = ? AND film_likes.user_id = ?";
    private static final String SELECT_MAX_INTERSECTION_USER_ID_QUERY = "SELECT fl1.user_id AS first_user, fl2.user_id AS second_user, COUNT(*) AS intersection_count " +
            "FROM film_likes fl1 " +
            "JOIN film_likes fl2 ON fl1.film_id = fl2.film_id AND fl1.user_id <> fl2.user_id AND fl1.user_id = ?" +
            "GROUP BY fl1.user_id, fl2.user_id " +
            "ORDER BY intersection_count DESC " +
            "LIMIT 1";

    @Override
    public Film getFilmById(Long filmId) {
        Optional<Film> film = jdbcTemplate.query(BASE_QUERY + SELECT_FILM_BY_ID_QUERY, filmRowMapper(), filmId)
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
        return jdbcTemplate.query(BASE_QUERY + SELECT_ALL_FILMS_QUERY, filmRowMapper());
    }

    @Override
    public List<Film> getCommonFilms(long userId, long friendId) {
        return jdbcTemplate.query(BASE_QUERY + SELECT_COMMON_FILMS_BY_USER_ID_AND_FRIEND_ID_QUERY, filmRowMapper(), userId, friendId);
    }

    @Override
    public List<Film> getPopularFilms(int count, Long genreId, Integer year) {
        StringBuilder queryBuilder = new StringBuilder(BASE_QUERY);

        if (genreId != null && year != null) {
            String query = queryBuilder
                    .append(SELECT_POPULAR_FILMS_QUERY_GENRE_FILTER)
                    .append(SELECT_POPULAR_FILMS_QUERY_YEAR_FILTER)
                    .append(SELECT_POPULAR_FILMS_QUERY_LIMIT)
                    .toString();
            return jdbcTemplate.query(query, filmRowMapper(), genreId, year, count);
        }
        if (genreId != null) {
            String query = queryBuilder
                    .append(SELECT_POPULAR_FILMS_QUERY_GENRE_FILTER)
                    .append(SELECT_POPULAR_FILMS_QUERY_LIMIT)
                    .toString();
            return jdbcTemplate.query(query, filmRowMapper(), genreId, count);
        } else if (year != null) {
            String query = queryBuilder
                    .append(SELECT_POPULAR_FILMS_QUERY_YEAR_FILTER)
                    .append(SELECT_POPULAR_FILMS_QUERY_LIMIT)
                    .toString();
            return jdbcTemplate.query(query, filmRowMapper(), year, count);
        } else {
            String query = queryBuilder
                    .append(SELECT_POPULAR_FILMS_QUERY_LIMIT)
                    .toString();
            return jdbcTemplate.query(query, filmRowMapper(), count);
        }
    }

    private List<Film> getUserLikedFilms(Long userId) {
        return jdbcTemplate.query(BASE_QUERY + SELECT_FILMS_BY_USER_ID_LIKE, filmRowMapper(), userId);
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
        setGenres(film, filmId);
        setDirectors(film, filmId);
        return filmId;
    }

    @Override
    public Long updateFilm(Film film) {
        final Long filmId = film.getId();

        jdbcTemplate.update(UPDATE_FILM_QUERY, film.getName(), film.getDescription(), film.getReleaseDate(), film.getDuration(), film.getMpa().getId(), filmId);
        jdbcTemplate.update(DELETE_FILM_GENRES_QUERY, filmId);
        jdbcTemplate.update(DELETE_FILM_DIRECTORS_QUERY, filmId);

        setGenres(film, filmId);
        setDirectors(film, filmId);
        return filmId;
    }

    @Override
    public void deleteFilm(long filmId) {
        jdbcTemplate.update(DELETE_FILM_QUERY, filmId);
    }

    @Override
    public void addLike(long filmId, long userId) {
        if (filmLikeExist(filmId, userId)) {
            return;
        }
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

    @Override
    public List<Film> getFilmsByDirector(long directorId, String sortBy) {
        List<Film> filmsByDirector;

        if (sortBy.equalsIgnoreCase(SortType.LIKES.name())) {
            filmsByDirector = jdbcTemplate.query(BASE_QUERY + SELECT_FILM_BY_DIRECTOR_SORT_BY_LIKES_QUERY, filmRowMapper(), directorId);
        } else if (sortBy.equalsIgnoreCase(SortType.YEAR.name())) {
            filmsByDirector = jdbcTemplate.query(BASE_QUERY + SELECT_FILM_BY_DIRECTOR_SORT_BY_YEAR_QUERY, filmRowMapper(), directorId);
        } else {
            throw new IllegalArgumentException("Неподдерживаемый параметр упорядочивания фильмов.");
        }

        if (filmsByDirector.isEmpty()) {
            throw new NotFoundException("Режиссёр #" + directorId + " не найден.");
        }

        return filmsByDirector;
    }

    @Override
    public List<Film> getFilmsBySearchQuery(String query, List<String> by) {
        List<Film> filmsBySearchQuery;

        if (by.size() == 2 && by.containsAll(List.of("title", "director"))) {
            filmsBySearchQuery = jdbcTemplate.query(
                    BASE_QUERY + SELECT_FILM_BY_SEARCH_QUERY_BY_TITLE_AND_DIRECTOR,
                    filmRowMapper(), "%" + query + "%", "%" + query + "%");
        } else if (by.size() == 1 && by.contains("title")) {
            filmsBySearchQuery = jdbcTemplate.query(
                    BASE_QUERY + SELECT_FILM_BY_SEARCH_QUERY_BY_TITLE,
                    filmRowMapper(), "%" + query + "%");
        } else if (by.size() == 1 && by.contains("director")) {
            filmsBySearchQuery = jdbcTemplate.query(
                    BASE_QUERY + SELECT_FILM_BY_SEARCH_QUERY_BY_DIRECTOR,
                    filmRowMapper(), "%" + query + "%");
        } else {
            throw new IllegalArgumentException("Переданы некорректные параметры поиска фильмов");
        }

        return filmsBySearchQuery;
    }

    private Long getMaxIntersectionUserId(Long userId) {
        Optional<Long> maxIntersectionUserId = jdbcTemplate.query(SELECT_MAX_INTERSECTION_USER_ID_QUERY,
                (rs, rowNum) -> rs.getLong("second_user"), userId).stream().findFirst();

        return maxIntersectionUserId.orElse(null);
    }

    @Override
    public List<Film> getRecommendations(Long userId) {
        Long maxIntersectionUserId = getMaxIntersectionUserId(userId);
        if (maxIntersectionUserId == null) {
            return new ArrayList<>();
        }
        List<Film> userFilms = getUserLikedFilms(userId);
        List<Film> filmsToRecommendations = getUserLikedFilms(maxIntersectionUserId);
        List<Film> recommendations = new ArrayList<>();
        for (Film film : filmsToRecommendations) {
            if (!userFilms.contains(film)) {
                recommendations.add(film);
            }
        }
        return recommendations;
    }

    private void setDirectors(Film film, Long filmId) {
        Set<Director> directors = film.getDirectors();
        if (Objects.nonNull(directors)) {
            directors.forEach(director -> jdbcTemplate.update(
                    INSERT_FILM_DIRECTORS_QUERY,
                    filmId,
                    director.getId()
            ));
        }
    }

    private void setGenres(Film film, Long filmId) {
        Set<Genre> genres = film.getGenres();
        if (Objects.nonNull(genres)) {
            genres.forEach(genre -> jdbcTemplate.update(
                    INSERT_FILM_GENRES_QUERY,
                    filmId,
                    genre.getId()
            ));
        }
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
            film.setDirectors(parseDirectors(rs.getString("directors")));

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

    private Set<Director> parseDirectors(String directorsString) {
        if (Objects.isNull(directorsString)) {
            return Collections.emptySet();
        }
        return Arrays.stream(directorsString.split(";"))
                .map(directorIdAndName -> {
                    String[] parts = directorIdAndName.split(",");
                    final long directorId = Long.parseLong(parts[0]);
                    String directorName = parts[1];
                    return new Director(directorId, directorName);
                })
                .collect(Collectors.toSet());
    }
}
