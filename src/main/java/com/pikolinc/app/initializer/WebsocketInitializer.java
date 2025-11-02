package com.pikolinc.app.initializer;

import com.pikolinc.ws.ItemListWebsocketHandler;
import com.pikolinc.ws.OfferWebSocketHandler;
import spark.Spark;

/**
 * Registers WebSocket endpoints used by the front-end clients.
 *
 * <p>Endpoints:
 * <ul>
 *   <li>/ws - item list updates</li>
 *   <li>/ws/offers - offers updates</li>
 * </ul>
 */
public class WebsocketInitializer implements Initializer{
    @Override
    public void init() {
        Spark.webSocket("/ws", ItemListWebsocketHandler.class);
        Spark.webSocket("/ws/offers", OfferWebSocketHandler.class);
    }
}
