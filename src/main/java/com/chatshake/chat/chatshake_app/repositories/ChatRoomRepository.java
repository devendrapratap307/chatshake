package com.chatshake.chat.chatshake_app.repositories;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import com.chatshake.chat.chatshake_app.models.ChatRoomBO;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ChatRoomRepository extends MongoRepository<ChatRoomBO,String> {
    ChatRoomBO findByRoomName(String roomName);
    List<ChatRoomBO> findByParticipantsInAndTypeAndStatus(String participant, ENUM.ROOM_TYPE type, ENUM.ROOM_STATUS status);
}
