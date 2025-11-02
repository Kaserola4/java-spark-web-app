package com.pikolinc.infraestructure.events;

import lombok.Getter;

/**
 * Generic event wrapper used by the in-memory EventBus.
 *
 * @param <T> payload type
 */
@Getter
public abstract class Event<T> {
    private final T payload;

    public Event(T payload) {
        this.payload = payload;
    }

    public abstract EventType getType();
}