package com.chatshake.chat.chatshake_app.repositories;

import com.chatshake.chat.chatshake_app.models.UserTempBO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserTempRepository extends MongoRepository<UserTempBO, String> {
}
