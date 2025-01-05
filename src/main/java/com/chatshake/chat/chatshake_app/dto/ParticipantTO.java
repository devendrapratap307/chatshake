package com.chatshake.chat.chatshake_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParticipantTO {
    private String id;
    private boolean adminFlag;

    private String label;
}
