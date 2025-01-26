package com.chatshake.chat.chatshake_app.constants;

public class ENUM {
    public enum REQUEST_TYPE {
        PENDING, ACCEPTED, DENIED, REJECTED
    }
    public enum ROOM_TYPE {
        CHAT, GROUP, SELF
    }
    public enum ROOM_STATUS {
        ACT, DEL, BLOCK
    }
    public enum MESSAGE_STATUS {
        ACT, DEL, EDIT
    }
    public enum MESSAGE_TYPE {
        JOIN, LEAVE, CHAT, OUTLAY
    }
    public enum SPLIT_TYPE {
        EQUAL, PERCENT, CUSTOM
    }

}
