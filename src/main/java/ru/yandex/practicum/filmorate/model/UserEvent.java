package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserEvent {
    @EqualsAndHashCode.Include
    private Long eventId;
    private Long userId;
    private Long entityId;
    private UserEventType eventType;
    private UserEventOperation operation;
    private Long timestamp;

    public UserEvent() {

    }

    public UserEvent(UserEventType userEventType, UserEventOperation userEventOperation, long userId, long entityId) {
        this.eventType = userEventType;
        this.operation = userEventOperation;
        this.userId = userId;
        this.entityId = entityId;
    }
}