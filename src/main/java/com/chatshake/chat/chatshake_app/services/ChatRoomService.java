package com.chatshake.chat.chatshake_app.services;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import com.chatshake.chat.chatshake_app.dto.*;
import com.chatshake.chat.chatshake_app.models.ChatRoomBO;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ChatRoomService {
    String roomRequest(RoomRequestTO roomRequest);
    String roomRequestAccetance(RoomRequestTO roomRequest, boolean acceptFlag);
    SearchRespTO searchRequests(SearchReqTO searchReq, boolean receiveFlag, boolean pageFlag);
    ChatRoomTO createChatRoom(String roomName, ENUM.ROOM_TYPE type, List<String> participants);
    ChatRoomTO createChatRoom(ChatRoomTO chatRoom);
    List<ChatRoomBO> findRooms(String participant, ENUM.ROOM_TYPE type, ENUM.ROOM_STATUS status);
    SearchRespTO searchRooms(SearchReqTO searchReq, boolean pageFlag);
    SearchRespTO searchMessages(SearchReqTO searchReq, boolean pageFlag);

    MessageRequestTO saveOrUpdateMessage(MessageRequestTO msg);



}
