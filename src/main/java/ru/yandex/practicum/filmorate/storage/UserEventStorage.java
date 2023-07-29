package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.UserEvent;

import java.util.List;

public interface UserEventStorage {
    List<UserEvent> getEventsByUserId(Long userId);

    Long addEvent(UserEvent userEvent);
}