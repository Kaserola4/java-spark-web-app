package com.pikolinc.app.initializer;

import com.pikolinc.infraestructure.events.listeners.EventListener;
import com.pikolinc.infraestructure.events.listeners.ItemsEventListener;
import com.pikolinc.infraestructure.events.listeners.OffersEventListener;
import java.util.List;

/**
 * Registers domain event listeners (items, offers).
 *
 * <p>Each listener subscribes to the EventBus for relevant events and forwards them
 * to WebSocket handlers for real-time distribution.
 */
public class EventListenersInitializer implements Initializer {
    @Override
    public void init() {
        List<EventListener>  eventListeners = List.of(
                new OffersEventListener(),
                new ItemsEventListener()
        );

        eventListeners.forEach(EventListener::listen);
    }
}
