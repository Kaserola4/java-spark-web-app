package com.pikolinc.ws;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class ItemListWebsocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(ItemListWebsocketHandler.class);
    private static final Gson gson = new Gson();

    // itemId -> sessions
    private static final Map<String, Set<Session>> itemSubscriptions = new ConcurrentHashMap<>();

    // session -> itemIds
    private static final Map<Session, Set<String>> sessionSubscriptions = new ConcurrentHashMap<>();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessionSubscriptions.put(session, ConcurrentHashMap.newKeySet());
        logger.info("{} Connected to item list", session.getRemoteAddress().getHostName());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        try {
            JsonObject json = gson.fromJson(message, JsonObject.class);
            String action = json.has("action") ? json.get("action").getAsString() : "";

            if ("subscribe".equalsIgnoreCase(action)) {
                json.getAsJsonArray("itemIds").forEach(id -> subscribe(session, id.getAsString()));
            } else if ("unsubscribe".equalsIgnoreCase(action)) {
                json.getAsJsonArray("itemIds").forEach(id -> unsubscribe(session, id.getAsString()));
            } else {
                logger.warn("Unknown action from {}: {}", session.getRemoteAddress(), message);
            }
        } catch (Exception e) {
            logger.warn("Failed to handle message from {}: {}", session.getRemoteAddress(), e.getMessage());
        }
    }


    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Set<String> items = sessionSubscriptions.get(session);

        if (items != null) {
            items.forEach(itemId -> {
                Set<Session> sessions = itemSubscriptions.get(itemId);

                if (sessions != null) sessions.remove(session);
            });
        }

        logger.info("Disconnected: {} ({})", session.getRemoteAddress(), reason);
    }

    public static void broadcastNewItem(String itemId, Object obj) {
        Set<Session> sessions = itemSubscriptions.get(itemId);

        if (sessions == null) return;

        sessions.removeIf(s -> !s.isOpen());

        String payload = gson.toJson(obj);

        for (Session s : sessions) {
            try {
                s.getRemote().sendString(payload);
            } catch (IOException e) {
                logger.warn("Failed to send new item to {}: {}", s.getRemoteAddress(), e.getMessage());
            }
        }
    }

    private void subscribe(Session session, String itemId) {
        itemSubscriptions.computeIfAbsent(itemId, k -> ConcurrentHashMap.newKeySet()).add(session);
        sessionSubscriptions.computeIfAbsent(session, k -> ConcurrentHashMap.newKeySet()).add(itemId);
        logger.info("{} subscribed to item {}", session.getRemoteAddress().getHostName(), itemId);
    }

    private void unsubscribe(Session session, String itemId) {
        Set<Session> sessions = itemSubscriptions.get(itemId);
        if (sessions != null) sessions.remove(session);
        Set<String> items = sessionSubscriptions.get(session);
        if (items != null) items.remove(itemId);
        logger.info("{} unsubscribed from item {}", session.getRemoteAddress().getHostName(), itemId);
    }
}
