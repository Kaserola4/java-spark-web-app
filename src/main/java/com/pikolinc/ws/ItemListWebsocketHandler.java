package com.pikolinc.ws;

import com.google.gson.Gson;
import com.pikolinc.domain.Item;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class ItemListWebsocketHandler {
    private static final Set<Session> sessions = ConcurrentHashMap.newKeySet();
    private static final Logger logger = LoggerFactory.getLogger(ItemListWebsocketHandler.class);
    private static final Gson gson = new Gson();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.add(session);
        logger.info("{} Connected to item list", session.getRemoteAddress().getHostName());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
        logger.info("Disconnected from item list: {} ({})", session.getRemoteAddress(), reason);
    }

    public static void broadcastNewItem(Item item) {
        sessions.removeIf(s -> !s.isOpen());

        for (Session s : sessions) {
            try {
                s.getRemote().sendString(gson.toJson(item));
            } catch (IOException e) {
                logger.warn("Failed to send new item to {}: {}", s.getRemoteAddress(), e.getMessage());
            }
        }
    }
}
