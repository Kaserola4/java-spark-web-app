package com.pikolinc.infraestructure.events.offer;

import com.pikolinc.dto.response.OfferResponseDto;
import com.pikolinc.infraestructure.events.Event;
import com.pikolinc.infraestructure.events.EventType;

public class OfferRebidEvent extends Event<OfferResponseDto> {
    public OfferRebidEvent(OfferResponseDto payload) {
        super(payload);
    }

    @Override
    public EventType getType() {
        return null;
    }
}
