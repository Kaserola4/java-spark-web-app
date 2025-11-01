package com.pikolinc.infraestructure.events.listeners;

import com.pikolinc.dto.response.OfferResponseDto;
import com.pikolinc.infraestructure.events.EventBus;
import com.pikolinc.infraestructure.events.EventType;
import com.pikolinc.services.OfferService;
import com.pikolinc.services.impl.ItemServiceImpl;
import com.pikolinc.services.impl.OfferServiceImpl;
import com.pikolinc.services.impl.UserServiceImpl;
import com.pikolinc.ws.OfferWebSocketHandler;

import java.util.Map;

public class OffersEventListener implements EventListener {
    private final OfferService offerService = new OfferServiceImpl(new ItemServiceImpl(), new UserServiceImpl());

    @Override
    public void listen() {
        Map<EventType, Class<?>> eventMappings = Map.ofEntries(
                Map.entry(EventType.OFFER_CREATED, OfferResponseDto.class),
                Map.entry(EventType.OFFER_UPDATED, OfferResponseDto.class),
                Map.entry(EventType.OFFER_REBID, OfferResponseDto.class),
                Map.entry(EventType.OFFER_ACCEPTED, Long.class),
                Map.entry(EventType.OFFER_COMPLETED, Long.class),
                Map.entry(EventType.OFFER_CANCELLED, Long.class),
                Map.entry(EventType.OFFER_REJECTED, Long.class),
                Map.entry(EventType.OFFER_DELETED, Long.class)
        );

        eventMappings.forEach((type, clazz) -> {
            if (clazz.equals(OfferResponseDto.class)) {
                EventBus.subscribe(type, OfferResponseDto.class,
                        offer -> OfferWebSocketHandler.broadCastMessage(type, offer.getItemId().toString(), offer));
            } else if (clazz.equals(Long.class)) {
                EventBus.subscribe(type, Long.class,
                        id -> {
                            OfferResponseDto offer = offerService.findById(id);
                            OfferWebSocketHandler.broadCastMessage(type, offer.getItemId().toString(), offer);
                        });
            }
        });
    }
}
