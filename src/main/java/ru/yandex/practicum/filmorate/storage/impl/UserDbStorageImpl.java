package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component("userDbStorage")
@RequiredArgsConstructor
@Slf4j
public class UserDbStorageImpl implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_USER_BY_ID_QUERY = "SELECT " +
            "users.id, " +
            "users.email, " +
            "users.login, " +
            "users.name, " +
            "users.birthday, " +
            "GROUP_CONCAT(friendships.to_user_id separator ',') AS friend_ids " +
            "FROM users " +
            "LEFT JOIN friendships ON users.id = friendships.from_user_id " +
            "WHERE users.id = ?" +
            "GROUP BY users.id";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT " +
            "users.id, " +
            "users.email, " +
            "users.login, " +
            "users.name, " +
            "users.birthday, " +
            "GROUP_CONCAT(friendships.to_user_id separator ',') AS friend_ids " +
            "FROM users " +
            "LEFT JOIN friendships ON users.id = friendships.from_user_id " +
            "GROUP BY users.id";
    private static final String UPDATE_USER_QUERY = "UPDATE users SET email = ?, login = ?, name = ?, birthday = ? WHERE id = ?";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String INSERT_FRIENDSHIP_QUERY = "INSERT INTO friendships (from_user_id, to_user_id) VALUES (?, ?)";
    private static final String DELETE_FRIENDSHIP_QUERY = "DELETE FROM friendships WHERE from_user_id = ? AND to_user_id = ?";

    @Override
    public User getUserById(Long userId) {
        Optional<User> users = jdbcTemplate.query(SELECT_USER_BY_ID_QUERY, userRowMapper(), userId)
                .stream()
                .findFirst();

        if (users.isEmpty()) {
            log.error("Пользователь #" + userId + " не найден.");
            throw new NotFoundException("Пользователь #" + userId + " не найден.");
        }

        return users.get();
    }

    @Override
    public List<User> getUsers() {
        return jdbcTemplate.query(SELECT_ALL_USERS_QUERY, userRowMapper());
    }

    @Override
    public Long createUser(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        Map<String, String> params = Map.of(
                "email", user.getEmail(),
                "login", user.getLogin(),
                "name", user.getName(),
                "birthday", user.getBirthday().toString()
        );

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    @Override
    public Long updateUser(User user) {
        final Long userId = user.getId();
        jdbcTemplate.update(UPDATE_USER_QUERY, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), userId);
        return userId;
    }

    @Override
    public void deleteUser(long userId) {
        jdbcTemplate.update(DELETE_USER_QUERY, userId);
    }

    public void addFriend(long userId, long friendId) {
        jdbcTemplate.update(INSERT_FRIENDSHIP_QUERY, userId, friendId);
    }

    @Override
    public void removeFriend(long userId, long friendId) {
        jdbcTemplate.update(DELETE_FRIENDSHIP_QUERY, userId, friendId);
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setEmail(rs.getString("email"));
            user.setLogin(rs.getString("login"));
            user.setName(rs.getString("name"));
            user.setBirthday(rs.getDate("birthday").toLocalDate());
            user.setFriendIds(parseFriendIds(rs.getString("friend_ids")));

            return user;
        };
    }

    private Set<Long> parseFriendIds(String friendIdsString) {
        if (Objects.isNull(friendIdsString)) {
            return Collections.emptySet();
        }

        return Arrays.stream(friendIdsString.split(","))
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }
}