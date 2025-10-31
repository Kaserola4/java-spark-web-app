package com.pikolinc.infraestructure.events.listeners;

import com.pikolinc.domain.Item;
import com.pikolinc.infraestructure.events.EventBus;
import com.pikolinc.infraestructure.events.EventType;
import com.pikolinc.ws.ItemListWebsocketHandler;

import java.util.Map;

public class ItemsEventListener implements EventListener {
    @Override
    public void listen() {
        Map<EventType, Class<?>> eventMappings = Map.of(
                EventType.ITEM_CREATED, Item.class,
                EventType.ITEM_UPDATED, Item.class,
                EventType.ITEM_DELETED, String.class
        );

        eventMappings.forEach((type, clazz) -> {
            if (clazz.equals(Item.class)) {
                EventBus.subscribe(type, Item.class,
                        item -> ItemListWebsocketHandler.brodCastMessage(type, item.getId().toString(), item));
            } else if (clazz.equals(String.class)) {
                EventBus.subscribe(type, String.class,
                        id -> ItemListWebsocketHandler.brodCastMessage(type, id, id));
            }
        });
    }
}
