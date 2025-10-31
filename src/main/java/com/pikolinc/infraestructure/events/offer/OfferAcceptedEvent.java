package com.pikolinc.infraestructure.events.offer;

import com.pikolinc.infraestructure.events.Event;
import com.pikolinc.infraestructure.events.EventType;

public class OfferAcceptedEvent extends Event<Long> {
    public OfferAcceptedEvent(Long payload) {
        super(payload);
    }

    @Override
    public EventType getType() {
        return EventType.OFFER_ACCEPTED;
    }
}
