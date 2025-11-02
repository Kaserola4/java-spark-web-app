package com.pikolinc.infraestructure.events;

/**
 * Enumeration of event types used across the app (ITEM_CREATED, OFFER_CREATED, etc.).
 */
public enum EventType {
    OFFER_CREATED,
    OFFER_REBID,
    OFFER_UPDATED,
    OFFER_ACCEPTED,
    OFFER_COMPLETED,
    OFFER_CANCELLED,
    OFFER_REJECTED,
    OFFER_DELETED,
    ITEM_CREATED,
    ITEM_UPDATED,
    ITEM_DELETED,
}
