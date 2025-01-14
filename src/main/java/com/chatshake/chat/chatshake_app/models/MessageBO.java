package com.chatshake.chat.chatshake_app.models;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "message")
public class MessageBO {
    @Id
    private String id;
    private String roomId;
    private String sender;
    private String content;
    private ENUM.MESSAGE_TYPE type;
    private ENUM.MESSAGE_STATUS status;
    private LocalDateTime timeStamp;
    public MessageBO(){}

    public MessageBO(String roomId, String sender, String content, ENUM.MESSAGE_TYPE type, ENUM.MESSAGE_STATUS status) {
        this.roomId = roomId;
        this.sender = sender;
        this.content = content;
        this.type = type;
        this.status = status;
        this.timeStamp = LocalDateTime.now();
    }
}
