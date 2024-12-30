package com.chatshake.chat.chatshake_app.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "message")
@NoArgsConstructor
public class MessageBO {
    private String roomId;
    private String sender;
    private String content;
    private LocalDateTime timeStamp;

    public MessageBO(String roomId, String sender, String content) {
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
        this.timeStamp = LocalDateTime.now();
    }



}
