package com.pikolinc.ws;

import com.google.gson.Gson;
import com.pikolinc.dto.response.OfferResponseDto;
import com.pikolinc.infraestructure.events.EventType;
import com.pikolinc.ws.message.WebSocketMessage;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class OfferWebSocketHandler {
    private static final ConcurrentHashMap<String, Set<Session>> offerSessions = new ConcurrentHashMap<>();
    private static final Logger logger = LoggerFactory.getLogger(OfferWebSocketHandler.class);
    private static final Gson gson = new Gson();

    @OnWebSocketConnect
    public void onConnect(Session session) {
        Long itemId = getItemIdFromQuery(session);

        if (itemId == null) {
            logger.warn("Connection rejected, missing itemId: {}", session.getRemoteAddress());
            session.close(4000, "Missing itemId");
            return;
        }

        offerSessions.computeIfAbsent(String.valueOf(itemId), k -> ConcurrentHashMap.newKeySet()).add(session);
        logger.info("{} Connected to item id {}", session.getRemoteAddress().getHostName(), itemId);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        Long itemId = getItemIdFromQuery(session);
        if (itemId == null) return;

        Set<Session> sessions = offerSessions.get(itemId);
        if (sessions != null) sessions.remove(session);

        logger.info("Disconnected from item {}: {} ({})", itemId, session.getRemoteAddress(), reason);
    }

    private Long getItemIdFromQuery(Session session) {
        String query = session.getUpgradeRequest().getQueryString();
        if (query == null || query.isBlank()) return null;

        for (String param : query.split("&")) {
            int idx = param.indexOf('=');
            if (idx > 0) {
                String key = param.substring(0, idx);
                String value = param.substring(idx + 1);
                if ("itemId".equals(key)) {
                    try {
                        value = URLDecoder.decode(value, StandardCharsets.UTF_8);
                        return Long.parseLong(value);
                    } catch (Exception e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    public static void broadCastMessage(EventType eventType, String itemId, Object obj) {
        Set<Session> sessions = offerSessions.get(itemId);
        if (sessions == null) return;

        WebSocketMessage message = WebSocketMessage.builder()
                .eventType(eventType)
                .data(obj)
                .build();

        for (Session s : Set.copyOf(sessions)) {
            if (s.isOpen()) {
                try {
                    s.getRemote().sendString(gson.toJson(message));
                } catch (IOException e) {
                    logger.warn("Failed to send message: {}", e.getMessage());
                }
            } else {
                sessions.remove(s);
            }
        }
    }
}
