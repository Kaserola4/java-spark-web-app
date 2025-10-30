package com.pikolinc.ws;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@WebSocket
public class ItemListWebsocketHandler {
    private static final ConcurrentHashMap<Session, String> sessions = new ConcurrentHashMap<>();
    private static final Logger logger =  LoggerFactory.getLogger(ItemListWebsocketHandler.class);
    @OnWebSocketConnect
    public void onConnect(Session session) {
        sessions.put(session, session.getRemoteAddress().toString());
        logger.info("Connected to {}", session.getRemoteAddress().toString());
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
}
