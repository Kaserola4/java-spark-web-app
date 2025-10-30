package com.pikolinc.infraestructure.events.listeners;

import com.pikolinc.domain.Item;
import com.pikolinc.infraestructure.events.EventBus;
import com.pikolinc.infraestructure.events.EventType;
import com.pikolinc.ws.ItemListWebsocketHandler;

public class ItemsEventListener implements EventListener {
    @Override
    public void listen() {
        EventBus.subscribe(
                EventType.ITEM_CREATED,
                Item.class,
                ItemListWebsocketHandler::broadcastNewItem);
    }
}
