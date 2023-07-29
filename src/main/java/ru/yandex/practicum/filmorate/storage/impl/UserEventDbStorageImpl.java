package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.model.UserEventOperation;
import ru.yandex.practicum.filmorate.model.UserEventType;
import ru.yandex.practicum.filmorate.storage.UserEventStorage;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserEventDbStorageImpl implements UserEventStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_ALL_USER_EVENTS_QUERY = "SELECT events.* " +
            "FROM user_events AS events " +
            "WHERE events.user_id = ? " +
            "ORDER BY events.id";

    @Override
    public List<UserEvent> getEventsByUserId(Long userId) {
        return jdbcTemplate.query(SELECT_ALL_USER_EVENTS_QUERY, eventRowMapper(), userId);
    }

    @Override
    public Long addEvent(UserEvent userEvent) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("user_events")
                .usingGeneratedKeyColumns("id");

        Map<String, String> params = Map.of(
                "user_id", userEvent.getUserId().toString(),
                "entity_id", userEvent.getEntityId().toString(),
                "event_type", userEvent.getEventType().toString(),
                "event_operation", userEvent.getOperation().toString(),
                "created_at", new java.sql.Timestamp(new java.util.Date().getTime()).toString()
        );

        return simpleJdbcInsert.executeAndReturnKey(params).longValue();
    }

    private RowMapper<UserEvent> eventRowMapper() {
        return (rs, rowNum) -> {
            UserEvent event = new UserEvent();
            event.setEventId(rs.getLong("id"));
            event.setUserId(rs.getLong("user_id"));
            event.setEntityId(rs.getLong("entity_id"));
            event.setEventType(UserEventType.valueOf(rs.getString("event_type")));
            event.setOperation(UserEventOperation.valueOf(rs.getString("event_operation")));
            event.setTimestamp(rs.getTimestamp("created_at").getTime());

            return event;
        };
    }
}