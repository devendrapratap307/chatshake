package com.chatshake.chat.chatshake_app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchReqTO {
    int page;
    int limit;

    String label;
    String name;
    String username;
}
