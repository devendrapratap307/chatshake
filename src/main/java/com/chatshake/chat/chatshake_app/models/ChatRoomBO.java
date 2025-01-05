package com.chatshake.chat.chatshake_app.models;

import com.chatshake.chat.chatshake_app.constants.ENUM;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "room")
public class ChatRoomBO extends AuditBO {
    @Id
    private String id;
    private String roomName;
    private ENUM.ROOM_TYPE type;
    private ENUM.ROOM_STATUS status;
    private List<ParticipantBO> participants;
    private boolean onlyAdmin;
}
