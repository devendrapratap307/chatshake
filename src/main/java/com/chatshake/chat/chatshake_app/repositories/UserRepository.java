package com.chatshake.chat.chatshake_app.repositories;


import com.chatshake.chat.chatshake_app.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}
