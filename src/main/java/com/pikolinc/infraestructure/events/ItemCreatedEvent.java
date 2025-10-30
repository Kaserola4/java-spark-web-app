package com.pikolinc.infraestructure.events;

import com.pikolinc.domain.Item;

public class ItemCreatedEvent extends Event<Item> {
    public ItemCreatedEvent(Item payload) {
        super(payload);
    }

    @Override
    public EventType getType() {
        return EventType.ITEM_CREATED;
    }
}
