package com.chatshake.chat.chatshake_app.controllers;

import com.chatshake.chat.chatshake_app.dto.MessageRequestTO;
import com.chatshake.chat.chatshake_app.models.ChatRoomBO;
import com.chatshake.chat.chatshake_app.models.MessageBO;
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
    public MessageBO sendMessage(@DestinationVariable String roomId, @RequestBody MessageRequestTO request){
        ChatRoomBO room = roomRepository.findByRoomId(request.getRoomId());

        MessageBO message = new MessageBO();
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
