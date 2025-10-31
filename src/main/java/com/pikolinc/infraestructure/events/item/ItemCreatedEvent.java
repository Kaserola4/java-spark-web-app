package com.pikolinc.infraestructure.events.item;

import com.pikolinc.domain.Item;
import com.pikolinc.infraestructure.events.Event;
import com.pikolinc.infraestructure.events.EventType;

public class ItemCreatedEvent extends Event<Item> {
    public ItemCreatedEvent(Item payload) {
        super(payload);
    }

    @Override
    public EventType getType() {
        return EventType.ITEM_CREATED;
    }
}
