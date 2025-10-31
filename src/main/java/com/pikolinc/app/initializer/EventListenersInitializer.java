package com.pikolinc.app.initializer;

import com.pikolinc.infraestructure.events.listeners.EventListener;
import com.pikolinc.infraestructure.events.listeners.ItemsEventListener;
import com.pikolinc.infraestructure.events.listeners.OffersEventListener;
import java.util.List;

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
