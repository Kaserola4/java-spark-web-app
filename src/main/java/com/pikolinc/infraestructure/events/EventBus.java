package com.pikolinc.infraestructure.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Simple in-app EventBus used to publish domain events to registered listeners.
 *
 * <p>Listeners are grouped by EventType and executed synchronously.
 */
public class EventBus {
    private static final Map<EventType, List<EventListener<?>>> listeners = new ConcurrentHashMap<>();

    private static <T> void registerListener(EventType type, EventListener<T> listener) {
        listeners.computeIfAbsent(type, k -> new ArrayList<>()).add(listener);
    }

    public static <T> void subscribe(EventType eventType, Class<T> clazz, Consumer<T> consumer) {
        registerListener(eventType, obj -> {
            if (!clazz.isInstance(obj)) {
                throw new IllegalArgumentException(
                        "Invalid event type for " + eventType + ". Expected " + clazz.getSimpleName()
                );
            }
            consumer.accept(clazz.cast(obj));
        });
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
