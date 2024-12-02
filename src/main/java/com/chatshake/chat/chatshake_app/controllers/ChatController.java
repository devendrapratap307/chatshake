package com.chatshake.chat.chatshake_app.controllers;

import com.chatshake.chat.chatshake_app.dto.MessageRequest;
import com.chatshake.chat.chatshake_app.models.ChatRoom;
import com.chatshake.chat.chatshake_app.models.Message;
import com.chatshake.chat.chatshake_app.repositories.ChatRoomRepository;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;

@Controller
public class ChatController {

    ChatRoomRepository roomRepository;

    public ChatController(ChatRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(@DestinationVariable String roomId, @RequestBody MessageRequest request){
        ChatRoom room = roomRepository.findByRoomId(request.getRoomId());

        Message message = new Message();
        message.setContent(request.getContent());
        message.setSender(request.getSender());
        message.setTimeStamp(LocalDateTime.now());

        if(room != null){
            room.getMessages().add(message);
            roomRepository.save(room);
        } else {
            throw new RuntimeException("Chat Room not found");
        }
        return message;
    }


}
