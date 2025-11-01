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
                EventType.ITEM_DELETED, Long.class
        );

        eventMappings.forEach((type, clazz) -> {
            if (clazz.equals(Item.class)) {
                EventBus.subscribe(type, Item.class, item -> {
                    // Notify item-specific subscribers
                    ItemListWebsocketHandler.broadcastMessage(type, item.getId().toString(), item);

                    // Notify everyone globally when created or updated
                    if (type == EventType.ITEM_CREATED || type == EventType.ITEM_UPDATED) {
                        ItemListWebsocketHandler.broadcastGlobal(type, item);
                    }
                });
            } else if (clazz.equals(Long.class)) {
                EventBus.subscribe(type, Long.class, id -> {
                    // Notify item-specific subscribers
                    ItemListWebsocketHandler.broadcastMessage(type, String.valueOf(id), id);

                    // Notify everyone globally so UI can remove it
                    ItemListWebsocketHandler.broadcastGlobal(EventType.ITEM_DELETED, id);
                });
            }
        });
    }
}
