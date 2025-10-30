package com.pikolinc.app.initializer;

import com.pikolinc.infraestructure.events.listeners.EventListener;
import com.pikolinc.infraestructure.events.listeners.ItemsEventListener;
import com.pikolinc.infraestructure.events.listeners.OffersEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

public class EventListenersInitializer implements Initializer {
    private static final Logger logger = LoggerFactory.getLogger(EventListenersInitializer.class);
    @Override
    public void init() {
        logger.info("Initializing EventListeners");

        List<EventListener>  eventListeners = List.of(
                new OffersEventListener(),
                new ItemsEventListener()
        );

        eventListeners.forEach(EventListener::listen);
    }
}
