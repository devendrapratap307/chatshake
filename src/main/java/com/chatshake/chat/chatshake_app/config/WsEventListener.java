package com.chatshake.chat.chatshake_app.config;

import com.chatshake.chat.chatshake_app.dto.OnlineStatusTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
@RequiredArgsConstructor
@Slf4j
public class WsEventListener {
    private final SimpMessageSendingOperations simpMessageSendingOperations;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

//    public void handleWsDisconnectListener(SessionDisconnectEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        String sender = (String) headerAccessor.getSessionAttributes().get("sender");
//        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
//        if(roomId!=null && sender != null){
//            log.info("User disconnected: {}", sender);
//            MessageRequestTO message = new MessageRequestTO();//.builder().type(ENUM.MESSAGE_TYPE.LEAVE).sender(username).build();
//            message.setSender(sender);
//            message.setRoomId(roomId);
//            message.setType(ENUM.MESSAGE_TYPE.LEAVE);
//            message.setStatus(ENUM.MESSAGE_STATUS.ACT);
//            message.setTimeStamp(LocalDateTime.now());
//            simpMessageSendingOperations.convertAndSend("topic/public", message);
//        }
//    }

    // Handle user login (WebSocket connection established)
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) throws InterruptedException{
//        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
//        StompHeaderAccessor headerAccessorNative = StompHeaderAccessor.wrap(event.getMessage());
//        String userId = headerAccessorNative.getFirstNativeHeader("userId"); // Retrieve userId from header
//        System.out.println("Native Headers: " + userId +" ========== "+headerAccessorNative.getFirstNativeHeader("userId")+"--------------"+headerAccessorNative);
//        System.out.println("headerAccessorNative in Listener: " + headerAccessorNative);
//        System.out.println("headerAccessor in Listener: " + headerAccessor);
//        Thread.sleep(5000); // Debugging purpose
//
//        Map<String, Object> attributes1 = headerAccessor.getSessionAttributes();
//        System.out.println("Attributes after delay1: " + attributes1);
//        Map<String, Object> attributes = headerAccessorNative.getSessionAttributes();
//        if (attributes != null) {
//            String userId1 = (String) attributes.get("userId");
//            System.out.println("Attributes userId: " + userId1);
//            if (userId1 != null) {
//                redisTemplate.opsForValue().set("user-online:" + userId, "true");
////                sendPendingNotifications(userId);
//            } else {
//                log.error("User ID is null in session attributes");
//            }
//        } else {
//            log.error("Session attributes are null");
//        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.wrap(event.getMessage());
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
//        String sender = (String) headerAccessor.getSessionAttributes().get("sender");
//        String roomId = (String) headerAccessor.getSessionAttributes().get("roomId");
        // Mark user as offline in Redis
        if(userId != null){
            System.out.println("###################################SessionDisconnectEvent - user-online: " + userId);
            log.info("SessionDisconnectEvent - user-online: " + userId);
            messagingTemplate.convertAndSend("/topic/online-status/"+userId  , new OnlineStatusTO(userId, false));
            redisTemplate.delete("user-online:" + userId);
        }
    }

}
