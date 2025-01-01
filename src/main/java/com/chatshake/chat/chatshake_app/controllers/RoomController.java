package com.chatshake.chat.chatshake_app.controllers;

import com.chatshake.chat.chatshake_app.constants.MSG_CONST;
import com.chatshake.chat.chatshake_app.dto.ResponseTO;
import com.chatshake.chat.chatshake_app.dto.RoomRequestTO;
import com.chatshake.chat.chatshake_app.repositories.ChatRoomRepository;
import com.chatshake.chat.chatshake_app.services.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chat-room")
public class RoomController {

    @Autowired
    private ChatRoomService roomService;

    private ChatRoomRepository roomRepository;

    private ResponseTO resp;
    public RoomController(ChatRoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }
//
//    @PostMapping
//    public ResponseEntity<?> createChatRoom (@RequestBody String roomId) {
//        if(roomRepository.findByRoomId(roomId) != null){
//            return ResponseEntity.badRequest().body("Chat Room already exists !");
//        }
//        ChatRoomBO room = new ChatRoomBO();
//        room.setRoomId(roomId);
//        ChatRoomBO savedChatRoom = roomRepository.save(room);
//        return ResponseEntity.status(HttpStatus.CREATED).body(room);
//    }
//
//    @GetMapping("/{roomId}")
//    public ResponseEntity<?> joinRoom(@PathVariable String  roomId){
//        ChatRoomBO room = roomRepository.findByRoomId(roomId);
//        if(room == null){
//            return ResponseEntity.badRequest().body("Chat Room not found !!");
//        }
//        return ResponseEntity.ok(room);
//    }
//
//    @GetMapping("/{roomId}/message")
//    public ResponseEntity<List<MessageBO>> getMessages(@PathVariable String roomId,
//                                                       @RequestParam(value ="page", defaultValue = "0", required = false) int page,
//                                                       @RequestParam(value = "size", defaultValue = "20", required = false) int size){
//        ChatRoomBO room = roomRepository.findByRoomId(roomId);
//        if(room == null){
//            return ResponseEntity.badRequest().build();
//        }
//        // pagination
//
////        List<MessageBO> messageList = room.getMessages();
//        return ResponseEntity.ok(null);
//    }
// new changes
    @PostMapping("/room-request")
    public ResponseEntity<?> newChatRoomRequest (@RequestBody RoomRequestTO roomReq) {
        String msgCode = this.roomService.roomRequest(roomReq);
        if(msgCode != null){
            resp = ResponseTO.build(200, msgCode,"/room-request", "roomRequest", MSG_CONST.MSG.get(msgCode));
        } else {
            resp = ResponseTO.build(400, "M006","/room-request", "roomRequest", msgCode);
        }
        return new ResponseEntity<>(resp,HttpStatus.CREATED);
    }
    @PostMapping("/room-request/acceptance")
    public ResponseEntity<?> newChatRoomRequest (@RequestBody RoomRequestTO roomReq, @RequestParam(value = "acceptFlag", required = false) final boolean acceptFlag) {
        String msgCode = this.roomService.roomRequestAccetance(roomReq, acceptFlag);
        if(msgCode != null){
            resp = ResponseTO.build(200, msgCode,"/room-request/acceptance", "roomRequest", MSG_CONST.MSG.get(msgCode));
        } else {
            resp = ResponseTO.build(400, "M006","/room-request/acceptance", "roomRequest", msgCode);
        }
        return new ResponseEntity<>(resp,HttpStatus.CREATED);
    }
}
