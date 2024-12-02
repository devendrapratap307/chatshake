package com.chatshake.chat.chatshake_app.controllers;

import com.chatshake.chat.chatshake_app.models.ChatRoom;
import com.chatshake.chat.chatshake_app.models.Message;
import com.chatshake.chat.chatshake_app.repositories.ChatRoomRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vi/rooms")
public class RoomController {

    private ChatRoomRepository roomRepository;
    public RoomController(ChatRoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }

    @PostMapping
    public ResponseEntity<?> createChatRoom (@RequestBody String roomId) {
        if(roomRepository.findByRoomId(roomId) != null){
            return ResponseEntity.badRequest().body("Chat Room already exists !");
        }
        ChatRoom room = new ChatRoom();
        room.setRoomId(roomId);
        ChatRoom savedChatRoom = roomRepository.save(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    @GetMapping("/{roomId}")
    public ResponseEntity<?> joinRoom(@PathVariable String  roomId){
        ChatRoom room = roomRepository.findByRoomId(roomId);
        if(room == null){
            return ResponseEntity.badRequest().body("Chat Room not found !!");
        }
        return ResponseEntity.ok(room);
    }

    @GetMapping("/{roomId}/message")
    public ResponseEntity<List<Message>> getMessages(@PathVariable String roomId,
    @RequestParam(value ="page", defaultValue = "0", required = false) int page,
                                                     @RequestParam(value = "size", defaultValue = "20", required = false) int size){
        ChatRoom room = roomRepository.findByRoomId(roomId);
        if(room == null){
            return ResponseEntity.badRequest().build();
        }
        // pagination

        List<Message> messageList = room.getMessages();
        return ResponseEntity.ok(messageList);
    }
}