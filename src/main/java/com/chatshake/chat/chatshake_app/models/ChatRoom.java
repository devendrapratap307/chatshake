package com.chatshake.chat.chatshake_app.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "room")
public class ChatRoom {
    private String id;
    private String roomId;
    private List<Message> messages = new ArrayList<>();
}