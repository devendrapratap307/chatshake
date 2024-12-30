package com.chatshake.chat.chatshake_app.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "room")
public class ChatRoomBO {
    @Id
    private String id;
    private String roomId;
//    private List<MessageBO> messages = new ArrayList<>(); // need to separate because of larger data
    private List<String> participants;
}
