package com.chatshake.chat.chatshake_app.config;

import com.chatshake.chat.chatshake_app.dto.MessageRequestTO;
import com.chatshake.chat.chatshake_app.dto.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WsEventListener {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    public void handleWsDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null){
            log.info("User disconnected: {}", username);
            var message = MessageRequestTO.builder().type(MessageType.LEAVE).sender(username).build();
            simpMessageSendingOperations.convertAndSend("topic/public", message);
        }
    }
}
