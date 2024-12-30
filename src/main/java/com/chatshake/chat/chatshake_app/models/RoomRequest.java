package com.chatshake.chat.chatshake_app.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@Document(collection = "room_request")
public class RoomRequest {
    @Id
    private String id;
    private String reqFrom;

    private String reqTo;
    private boolean accepted;
}