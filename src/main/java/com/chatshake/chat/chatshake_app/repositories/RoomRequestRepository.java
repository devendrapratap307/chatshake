package com.chatshake.chat.chatshake_app.repositories;

import com.chatshake.chat.chatshake_app.models.RoomRequestBO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoomRequestRepository extends MongoRepository<RoomRequestBO,String> {
}
