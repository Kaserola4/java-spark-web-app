package com.pikolinc.infraestructure.events;

import lombok.Getter;

@Getter
public abstract class Event<T> {
    private final T payload;

    public Event(T payload) {
        this.payload = payload;
    }

    public abstract EventType getType();
}