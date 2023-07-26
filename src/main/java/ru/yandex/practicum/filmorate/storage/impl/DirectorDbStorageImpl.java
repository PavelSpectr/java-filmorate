package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.DirectorStorage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("directorDbStorage")
@RequiredArgsConstructor
@Slf4j
public class DirectorDbStorageImpl implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_DIRECTOR_BY_ID_QUERY = "SELECT directors.id, directors.name" +
            " FROM directors WHERE id = ?";
    private static final String SELECT_ALL_DIRECTORS_QUERY = "SELECT directors.id, directors.name" +
            " FROM directors";
    private static final String UPDATE_DIRECTOR_QUERY = "UPDATE directors SET name = ? WHERE id = ?";
    private static final String DELETE_DIRECTOR_QUERY = "DELETE FROM directors WHERE id = ?";

    @Override
    public List<Director> getDirectors() {
        return jdbcTemplate.query(SELECT_ALL_DIRECTORS_QUERY, directorRowMapper());
    }

    @Override
    public Director getDirectorById(long directorId) {
        Optional<Director> director = jdbcTemplate.query(SELECT_DIRECTOR_BY_ID_QUERY, directorRowMapper(), directorId)
                .stream()
                .findFirst();

        if (director.isEmpty()) {
            log.error("Режиссёр #" + directorId + " не найден.");
            throw new NotFoundException("Режиссёр #" + directorId + " не найден.");
        }

        return director.get();
    }

    @Override
    public long addDirector(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("directors")
                .usingGeneratedKeyColumns("id");

        Map<String, String> params = Map.of(
                "name", director.getName()
        );

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public long updateDirector(Director director) {
        final long directorId = director.getId();
        jdbcTemplate.update(UPDATE_DIRECTOR_QUERY, director.getName(), directorId);
        return directorId;
    }

    @Override
    public void deleteDirector(long directorId) {
        jdbcTemplate.update(DELETE_DIRECTOR_QUERY, directorId);
    }

    private RowMapper<Director> directorRowMapper() {
        return (rs, rowNum) -> new Director(rs.getLong("id"), rs.getString("name"));
    }
}
