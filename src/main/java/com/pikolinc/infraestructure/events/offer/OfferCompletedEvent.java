package com.pikolinc.infraestructure.events.offer;

import com.pikolinc.infraestructure.events.Event;
import com.pikolinc.infraestructure.events.EventType;

public class OfferCompletedEvent extends Event<Long> {
    public OfferCompletedEvent(Long payload) {
        super(payload);
    }

    @Override
    public EventType getType() {
        return EventType.OFFER_COMPLETED;
    }
}
