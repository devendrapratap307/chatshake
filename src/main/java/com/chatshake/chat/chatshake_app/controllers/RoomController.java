package com.chatshake.chat.chatshake_app.controllers;

import com.chatshake.chat.chatshake_app.constants.MSG_CONST;
import com.chatshake.chat.chatshake_app.dto.*;
import com.chatshake.chat.chatshake_app.repositories.ChatRoomRepository;
import com.chatshake.chat.chatshake_app.services.ChatRoomService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/chat-room")
@CrossOrigin("*")
public class RoomController {

    @Autowired
    private ChatRoomService roomService;

    private ChatRoomRepository roomRepository;

    private ResponseTO resp;
    public RoomController(ChatRoomRepository roomRepository){
        this.roomRepository = roomRepository;
    }
    @GetMapping("/messages/{roomId}")
    public ResponseEntity<?> getMessages(@PathVariable String roomId,
                                                       @RequestParam(value ="page", defaultValue = "0", required = false) int page,
                                                       @RequestParam(value = "size", defaultValue = "20", required = false) int size){
        SearchReqTO searchReq = new SearchReqTO();
        searchReq.setRoomId(roomId);
        searchReq.setPage(page);
        searchReq.setLimit(size);
        SearchRespTO searchResp = this.roomService.searchMessages(searchReq, true);
        resp = ResponseTO.build(200, "M001","/messages/", "roomMessage", searchResp);
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }
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

    @PostMapping("/room-request/search")
    public ResponseEntity<?> searchRoomRequest(@RequestBody SearchReqTO searchReqTO, @RequestParam(value = "pageFlag", required = false) final boolean pageFlag, @RequestParam(value = "receiveFlag", required = false) final boolean receiveFlag,Errors result, HttpServletRequest request) {
        SearchRespTO searchResp = this.roomService.searchRequests(searchReqTO,receiveFlag, pageFlag);
        resp = ResponseTO.build(200, "M001","/room-request/search", "roomRequest", searchResp);
        return new ResponseEntity<>(resp,HttpStatus.CREATED);
    }

    @PostMapping("/room/search")
    public ResponseEntity<?> searchChatRoom(@RequestBody SearchReqTO searchReqTO, @RequestParam(value = "pageFlag", required = false) final boolean pageFlag, Errors result, HttpServletRequest request) {
        SearchRespTO searchResp = this.roomService.searchRooms(searchReqTO, pageFlag);
        resp = ResponseTO.build(200, "M001","/user/search", "chatRoom", searchResp);
        return new ResponseEntity<>(resp,HttpStatus.CREATED);
    }

    @PostMapping("/room/add")
    public ResponseEntity<?> addChatRoom(@RequestBody ChatRoomTO chatRoom, Errors result, HttpServletRequest request) {
        resp = ResponseTO.build(200, "M001","/room/add", "chatRoom", this.roomService.createChatRoom(chatRoom));
        return new ResponseEntity<>(resp,HttpStatus.CREATED);
    }

    @GetMapping("/room/{roomId}")
    public ResponseEntity<?> getChatRoomById(@PathVariable String roomId){
        resp = ResponseTO.build(200, "M001","/room/{roomId}", "chatRoom", this.roomService.chatRoomById(roomId));
        return new ResponseEntity<>(resp, HttpStatus.CREATED);
    }

    @PutMapping("/room/update")
    public ResponseEntity<?> updateChatRoom(@RequestBody ChatRoomTO chatRoom, Errors result, HttpServletRequest request) {
        resp = ResponseTO.build(200, "M001","/room/update", "chatRoom", this.roomService.createChatRoom(chatRoom));
        return new ResponseEntity<>(resp,HttpStatus.CREATED);
    }
}
