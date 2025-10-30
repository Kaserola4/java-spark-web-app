package com.pikolinc.ws;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class ItemOfferWebSocketHandler {
    private static final ConcurrentHashMap<Session, String> sessions = new ConcurrentHashMap<>();
    private static Logger logger = LoggerFactory.getLogger(ItemOfferWebSocketHandler.class);

    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.put(session, session.getRemoteAddress().toString());
        String itemId = getItemIdFromQuery(session);
        logger.info("Connected to item id {}", itemId);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        for (Session s : sessions.keySet()) {
            if (s.isOpen()) {
                s.getRemote().sendString("Echo: " + message);
            }
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        sessions.remove(session);
        logger.info("Disconnected, reason: {}", reason);
    }

    private String getItemIdFromQuery(Session session) {
        String query = session.getUpgradeRequest().getQueryString();
        if (query == null || query.isBlank()) return null;

        for (String param : query.split("&")) {
            int idx = param.indexOf('=');
            if (idx > 0) {
                String k = param.substring(0, idx);
                String v = param.substring(idx + 1);
                if (k.equals("itemId")) {
                    try {
                        return java.net.URLDecoder.decode(v, StandardCharsets.UTF_8);
                    } catch (Exception e) {
                        return v;
                    }
                }
            }
        }
        return null;
    }
}
