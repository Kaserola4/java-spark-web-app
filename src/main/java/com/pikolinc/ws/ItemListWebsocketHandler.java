package com.pikolinc.ws;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pikolinc.infraestructure.events.EventType;
import com.pikolinc.ws.message.WebSocketMessage;
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

    // Sessions subscribed to all global events (e.g., ITEM_CREATED)
    private static final Set<Session> globalSubscribers = ConcurrentHashMap.newKeySet();

    // itemId -> sessions subscribed to that item
    private static final Map<String, Set<Session>> itemSubscriptions = new ConcurrentHashMap<>();

    // session -> itemIds it subscribed to
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
            } else if ("subscribeAll".equalsIgnoreCase(action)) {
                globalSubscribers.add(session);
                logger.info("{} subscribed to ALL items", session.getRemoteAddress().getHostName());
            } else if ("unsubscribeAll".equalsIgnoreCase(action)) {
                globalSubscribers.remove(session);
                logger.info("{} unsubscribed from ALL items", session.getRemoteAddress().getHostName());
            } else {
                logger.warn("Unknown action from {}: {}", session.getRemoteAddress(), message);
            }
        } catch (Exception e) {
            logger.warn("Failed to handle message from {}: {}", session.getRemoteAddress(), e.getMessage());
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        // Remove session from item subscriptions
        Set<String> items = sessionSubscriptions.get(session);
        if (items != null) {
            items.forEach(itemId -> {
                Set<Session> sessions = itemSubscriptions.get(itemId);
                if (sessions != null) sessions.remove(session);
            });
        }

        // Remove from globals
        globalSubscribers.remove(session);
        sessionSubscriptions.remove(session);

        logger.info("Disconnected: {} ({})", session.getRemoteAddress(), reason);
    }

    /**
     * Broadcasts a message to all sessions subscribed to a specific item.
     */
    public static void broadcastMessage(EventType eventType, String itemId, Object obj) {
        Set<Session> sessions = itemSubscriptions.get(itemId);
        if (sessions == null || sessions.isEmpty()) return;

        sessions.removeIf(s -> !s.isOpen());

        WebSocketMessage message = WebSocketMessage.builder()
                .eventType(eventType)
                .data(obj)
                .build();

        for (Session s : sessions) {
            try {
                s.getRemote().sendString(gson.toJson(message));
            } catch (IOException e) {
                logger.warn("Failed to send item message to {}: {}", s.getRemoteAddress(), e.getMessage());
            }
        }
    }

    /**
     * Broadcasts a message to all global subscribers (e.g., for ITEM_CREATED).
     */
    public static void broadcastGlobal(EventType eventType, Object obj) {
        if (globalSubscribers.isEmpty()) return;

        globalSubscribers.removeIf(s -> !s.isOpen());

        WebSocketMessage message = WebSocketMessage.builder()
                .eventType(eventType)
                .data(obj)
                .build();

        for (Session s : globalSubscribers) {
            try {
                s.getRemote().sendString(gson.toJson(message));
            } catch (IOException e) {
                logger.warn("Failed to send global event to {}: {}", s.getRemoteAddress(), e.getMessage());
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
