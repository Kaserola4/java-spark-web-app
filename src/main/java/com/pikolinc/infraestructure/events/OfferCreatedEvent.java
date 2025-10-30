package com.pikolinc.infraestructure.events;

import com.pikolinc.dto.response.OfferResponseDto;

public class OfferCreatedEvent extends Event<OfferResponseDto>{
    public OfferCreatedEvent(OfferResponseDto payload) {
        super(payload);
    }

    @Override
    public EventType getType() {
        return EventType.OFFER_CREATED;
    }
}
