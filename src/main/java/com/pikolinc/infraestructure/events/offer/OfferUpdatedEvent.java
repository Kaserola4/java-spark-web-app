package com.pikolinc.infraestructure.events.offer;

import com.pikolinc.dto.response.OfferResponseDto;
import com.pikolinc.infraestructure.events.Event;
import com.pikolinc.infraestructure.events.EventType;

public class OfferUpdatedEvent extends Event<OfferResponseDto> {
    public OfferUpdatedEvent(OfferResponseDto payload) {
        super(payload);
    }

    @Override
    public EventType getType() {
        return EventType.OFFER_UPDATED;
    }
}
