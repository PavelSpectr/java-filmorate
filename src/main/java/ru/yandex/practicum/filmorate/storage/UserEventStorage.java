package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.UserEvent;
import ru.yandex.practicum.filmorate.model.UserEventOperation;
import ru.yandex.practicum.filmorate.model.UserEventType;

import java.util.List;

public interface UserEventStorage {
    UserEvent getEventById(Long eventId);
    List<UserEvent> getEventsByUserId(Long userId);
    Long addEvent(UserEventType eventType, UserEventOperation operation, Long userId, Long entityId);
}