package com.chatshake.chat.chatshake_app.controllers;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import com.chatshake.chat.chatshake_app.dto.MessageRequestTO;
import com.chatshake.chat.chatshake_app.models.ChatRoomBO;
import com.chatshake.chat.chatshake_app.repositories.ChatRoomRepository;
import com.chatshake.chat.chatshake_app.services.ChatRoomService;
import com.chatshake.chat.chatshake_app.services.RedisStreamService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.util.Optional;

import static io.lettuce.core.pubsub.PubSubOutput.Type.message;

@Controller
public class ChatController {

    ChatRoomRepository roomRepository;

    @Autowired
    private ChatRoomService roomService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RedisStreamService redisStreamService;

    @Autowired
    private final ObjectMapper objectMapper;

    public ChatController(ChatRoomRepository roomRepository, ObjectMapper objectMapper) {
        this.roomRepository = roomRepository;
        this.objectMapper = objectMapper;
    }

    @MessageMapping("/connect")
    public void handleWebSocketConnect(SimpMessageHeaderAccessor headerAccessor) {
        String userId = (String) headerAccessor.getSessionAttributes().get("userId");
        if (userId != null) {
            this.roomService.userConnection(userId);
        } else {
            System.out.println("User ID is not available in the session.");
        }
    }

    @MessageMapping("chat.sendMessage/{roomId}")
    @SendTo("/topic/public/{roomId}")
    public MessageRequestTO sendMessage(@DestinationVariable String roomId, @Payload MessageRequestTO msg, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("sender", msg.getSender());
        headerAccessor.getSessionAttributes().put("roomId", msg.getRoomId());
//        System.out.println("Sender: "+msg.getSender()+" --- content: "+msg.getContent());
        msg.setTimeStamp(LocalDateTime.now());
        Optional<ChatRoomBO> room = roomRepository.findById(roomId);
        if(room.isPresent() && room.get().getStatus() != null && !room.get().getStatus().equals(ENUM.ROOM_STATUS.BLOCK)){
            redisStreamService.addMessageToStream("chat-stream", msg);
            if(room.get().getParticipants()!=null && !room.get().getParticipants().isEmpty()){
                room.get().getParticipants().forEach(ptsRow->{
                    if(ptsRow.getId() != null && msg.getSender() != null && !ptsRow.getId().equals(msg.getSender())){
                        String valueReturn = (String) redisTemplate.opsForValue().get("user-online:" + ptsRow.getId());
                        boolean isOnline = valueReturn != null && "true".equals(valueReturn);
                        if (!isOnline) {
                            String jsonMessage = null;
                            try {
                                jsonMessage = objectMapper.writeValueAsString(msg);
                            } catch (JsonProcessingException e) {
                                throw new RuntimeException(e);
                            }
                            redisTemplate.opsForList().leftPush("offline-queue:" + ptsRow.getId(), jsonMessage);
                        } else {
                            messagingTemplate.convertAndSend("/topic/private/" +ptsRow.getId(), msg);
                        }
                    }
                });
            }
        } else {
            throw new RuntimeException("Chat blocked or not found");
        }
        return msg;
    }

    @MessageMapping("chat.addUser")
    @SendTo("/topic/chat")
    public MessageRequestTO addUser(@Payload MessageRequestTO msg, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("userId", msg.getSender());
        System.out.println("User joined: "+msg.getSender());
        return msg;
    }

}
