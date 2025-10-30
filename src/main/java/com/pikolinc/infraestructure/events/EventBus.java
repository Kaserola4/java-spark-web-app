package com.pikolinc.infraestructure;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventBus {
    private static final Map<String, List<EventListener<?>>> listeners = new ConcurrentHashMap<>();

    public static <T> void subscribe(String eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new ArrayList<>()).add(listener);
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
