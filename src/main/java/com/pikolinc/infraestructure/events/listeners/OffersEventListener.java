package com.pikolinc.infraestructure.events.listeners;

import com.pikolinc.dto.response.OfferResponseDto;
import com.pikolinc.infraestructure.events.EventBus;
import com.pikolinc.infraestructure.events.EventType;
import com.pikolinc.ws.OfferWebSocketHandler;

public class OffersEventListener implements EventListener {
    @Override
    public void listen() {
        EventBus.subscribe(
                EventType.OFFER_CREATED,
                OfferResponseDto.class,
                offer -> OfferWebSocketHandler.broadCastMessage(EventType.OFFER_CREATED, offer.getId().toString(), offer)
        );

        EventBus.subscribe(
                EventType.OFFER_REBID,
                OfferResponseDto.class,
                offer -> OfferWebSocketHandler.broadCastMessage(EventType.OFFER_REBID, offer.getId().toString(), offer)
        );
    }
}