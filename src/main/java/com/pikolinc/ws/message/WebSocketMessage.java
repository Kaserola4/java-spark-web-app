package com.pikolinc.ws.message;

import com.pikolinc.infraestructure.events.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class WebSocketMessage {
    private final EventType eventType;
    private final Object data;
}
