package com.chatshake.chat.chatshake_app.repositories;

import com.chatshake.chat.chatshake_app.models.ChatRoomBO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatRoomRepository extends MongoRepository<ChatRoomBO,String> {
    ChatRoomBO findByRoomId(String roomId);
}
