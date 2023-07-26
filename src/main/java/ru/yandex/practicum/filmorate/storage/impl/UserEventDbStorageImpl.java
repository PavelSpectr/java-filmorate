package ru.yandex.practicum.filmorate.storage.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.model.UserEventOperation;
import ru.yandex.practicum.filmorate.model.UserEventType;
import ru.yandex.practicum.filmorate.storage.UserEventStorage;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component("userEventDbStorage")
@RequiredArgsConstructor
@Slf4j
public class UserEventDbStorageImpl implements UserEventStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String SELECT_USER_EVENT_BY_ID_QUERY = "SELECT " +
            "events.id, " +
            "events.user_id, " +
            "events.entity_id, " +
            "events.event_type, " +
            "events.event_operation, " +
            "events.created_at " +
            "FROM user_events AS events " +
            "WHERE events.id = ?";
    private static final String SELECT_ALL_USER_EVENTS_QUERY = "SELECT " +
            "events.id, " +
            "events.user_id, " +
            "events.entity_id, " +
            "events.event_type, " +
            "events.event_operation, " +
            "events.created_at " +
            "FROM user_events AS events " +
            "WHERE events.user_id = ? " +
            "ORDER BY events.id";

    @Override
    public UserEvent getEventById(Long eventId) {
        Optional<UserEvent> event = jdbcTemplate.query(SELECT_USER_EVENT_BY_ID_QUERY, eventRowMapper(), eventId)
                .stream()
                .findFirst();

        if (event.isEmpty()) {
            String errorMessage = "Событие #" + eventId + " не найдено.";
            log.error(errorMessage);
            throw new NotFoundException(errorMessage);
        }

        return event.get();
    }

    @Override
    public List<UserEvent> getEventsByUserId(Long userId) {
        return jdbcTemplate.query(SELECT_ALL_USER_EVENTS_QUERY, eventRowMapper(), userId);
    }

    @Override
    public Long addEvent(UserEventType eventType, UserEventOperation operation, Long userId, Long entityId) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate.getDataSource())
                .withTableName("user_events")
                .usingGeneratedKeyColumns("id");

        Map<String, String> params = Map.of(
                "user_id", userId.toString(),
                "entity_id", entityId.toString(),
                "event_type", eventType.toString(),
                "event_operation", operation.toString(),
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