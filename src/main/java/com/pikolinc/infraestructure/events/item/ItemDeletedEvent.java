package com.pikolinc.infraestructure.events.item;

import com.pikolinc.infraestructure.events.Event;
import com.pikolinc.infraestructure.events.EventType;

public class ItemDeletedEvent extends Event<Long> {
    public ItemDeletedEvent(long payload) {
        super(payload);
    }

    @Override
    public EventType getType() {
        return EventType.ITEM_DELETED;
    }
}
