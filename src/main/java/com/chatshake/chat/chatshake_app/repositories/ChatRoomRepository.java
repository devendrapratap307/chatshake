package com.chatshake.chat.chatshake_app.repositories;

import com.chatshake.chat.chatshake_app.models.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoom,String> {
    ChatRoom findByRoomId(String roomId);
}
