package com.pikolinc.infraestructure.events.listeners;

import com.pikolinc.dto.response.OfferResponseDto;
import com.pikolinc.infraestructure.events.EventBus;
import com.pikolinc.infraestructure.events.EventType;
import com.pikolinc.ws.ItemOfferWebSocketHandler;

public class OffersEventListener implements EventListener {
    @Override
    public void listen() {
        EventBus.subscribe(
                EventType.OFFER_CREATED,
                OfferResponseDto.class,
                offer -> ItemOfferWebSocketHandler.broadcastToItem(offer.getId().toString(), offer)
        );
    }
}