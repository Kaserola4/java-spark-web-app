package com.pikolinc.app.initializer;

import com.pikolinc.ws.ItemListWebsocketHandler;
import com.pikolinc.ws.ItemOfferWebSocketHandler;
import spark.Spark;

public class WebsocketInitializer implements Initializer{
    @Override
    public void init() {
        Spark.webSocket("/ws", ItemListWebsocketHandler.class);
        Spark.webSocket("/ws/item", ItemOfferWebSocketHandler.class);
    }
}
