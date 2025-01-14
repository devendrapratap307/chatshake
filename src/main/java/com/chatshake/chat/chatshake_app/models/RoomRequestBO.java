package com.chatshake.chat.chatshake_app.models;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "room_request")
public class RoomRequestBO {
    @Id
    private String id;
    private String reqFrom;
    private String reqTo;
    private ENUM.REQUEST_TYPE status;
}