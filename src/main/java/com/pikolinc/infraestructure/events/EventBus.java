package com.pikolinc.infraestructure.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {
    private static final Map<EventType, List<EventListener<?>>> listeners = new ConcurrentHashMap<>();

    public static <T> void subscribe(EventType type, EventListener<T> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }
    public static <T> void publish(Event<T> event) {
        List<EventListener<?>> ls = listeners.get(event.getType());
        if (ls == null) return;

        for (EventListener<?> listener : ls) {
            ((EventListener<T>) listener).handle(event.getPayload());
        }
    }

    public interface EventListener<T> {
        void handle(T payload);
    }
}
