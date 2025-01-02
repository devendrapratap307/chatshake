package com.chatshake.chat.chatshake_app.config;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import com.chatshake.chat.chatshake_app.dto.MessageRequestTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class WsEventListener {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    public void handleWsDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sender = (String) headerAccessor.getSessionAttributes().get("sender");
        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        if(roomId!=null && sender != null){
            log.info("User disconnected: {}", sender);
            MessageRequestTO message = new MessageRequestTO();//.builder().type(ENUM.MESSAGE_TYPE.LEAVE).sender(username).build();
            message.setSender(sender);
            message.setRoomId(roomId);
            message.setType(ENUM.MESSAGE_TYPE.LEAVE);
            message.setStatus(ENUM.MESSAGE_STATUS.ACT);
            message.setTimeStamp(LocalDateTime.now());
            simpMessageSendingOperations.convertAndSend("topic/public", message);
        }
    }
}
