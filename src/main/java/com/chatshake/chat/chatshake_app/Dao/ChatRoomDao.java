package com.chatshake.chat.chatshake_app.Dao;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import com.chatshake.chat.chatshake_app.dto.RoomRequestTO;
import com.chatshake.chat.chatshake_app.dto.SearchReqTO;
import com.chatshake.chat.chatshake_app.dto.SearchRespTO;
import com.chatshake.chat.chatshake_app.models.ChatRoomBO;
import com.chatshake.chat.chatshake_app.models.MessageBO;
import com.chatshake.chat.chatshake_app.models.RoomRequestBO;

import java.util.List;

public interface ChatRoomDao {
    List<RoomRequestBO> findRoomRequests(RoomRequestTO roomRequest, List<ENUM.REQUEST_TYPE> excludedStatuses);
    SearchRespTO searchRequests(SearchReqTO searchReq, boolean receiveFlag, boolean pageFlag);
    List<ChatRoomBO> findRooms(String participant, ENUM.ROOM_TYPE type, ENUM.ROOM_STATUS status);
    SearchRespTO searchRooms(SearchReqTO searchReq, boolean pageFlag);
    SearchRespTO searchMessages(SearchReqTO searchReq, boolean pageFlag);

    MessageBO saveOrUpdateMessage(MessageBO message);

}
