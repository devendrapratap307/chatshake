package com.chatshake.chat.chatshake_app.controllers;

import com.chatshake.chat.chatshake_app.dto.MessageRequestTO;
import com.chatshake.chat.chatshake_app.models.ChatRoomBO;
import com.chatshake.chat.chatshake_app.models.MessageBO;
import com.chatshake.chat.chatshake_app.repositories.ChatRoomRepository;
import com.chatshake.chat.chatshake_app.services.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class ChatController {

    ChatRoomRepository roomRepository;

    @Autowired
    private ChatRoomService roomService;

    public ChatController(ChatRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }


//    @MessageMapping("/sendMessage/{roomId}")
//    @SendTo("/topic/room/{roomId}")
//    public MessageBO sendMessage(@DestinationVariable String roomId, @RequestBody MessageRequestTO request){
//        ChatRoomBO room = roomRepository.findByRoomId(request.getRoomId());
//
//        MessageBO message = new MessageBO();
//        message.setContent(request.getContent());
//        message.setSender(request.getSender());
//        message.setTimeStamp(LocalDateTime.now());
//
//        if(room != null){
//            room.getMessages().add(message);
//            roomRepository.save(room);
//        } else {
//            throw new RuntimeException("Chat Room not found");
//        }
//        return message;
//    }

    @MessageMapping("chat.sendMessage/{roomId}")
    @SendTo("/topic/public/{roomId}")
    public MessageRequestTO sendMessage(@DestinationVariable String roomId, @Payload MessageRequestTO msg, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("sender", msg.getSender());
        headerAccessor.getSessionAttributes().put("roomId", msg.getRoomId());
        System.out.println("Sender: "+msg.getSender()+" --- content: "+msg.getContent());
        Optional<ChatRoomBO> room = roomRepository.findById(roomId);
        MessageRequestTO message = null;
        if(room.isPresent()){
            message = this.roomService.saveOrUpdateMessage(msg);
        } else {
            throw new RuntimeException("Chat Room not found");
        }
        return message;
    }

    @MessageMapping("chat.addUser")
    @SendTo("/topic/chat")
    public MessageRequestTO addUser(@Payload MessageRequestTO msg, SimpMessageHeaderAccessor headerAccessor){
        headerAccessor.getSessionAttributes().put("username", msg.getSender());
        System.out.println("User joined: "+msg.getSender());
        return msg;
    }

}
