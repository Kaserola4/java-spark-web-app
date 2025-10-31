package com.pikolinc.infraestructure.events.offer;

import com.pikolinc.infraestructure.events.Event;
import com.pikolinc.infraestructure.events.EventType;

public class OfferRejectedEvent extends Event<Long> {
    public OfferRejectedEvent(Long payload) {
        super(payload);
    }

    @Override
    public EventType getType() {
        return null;
    }
}
