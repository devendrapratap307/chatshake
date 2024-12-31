package com.chatshake.chat.chatshake_app.constants;

import java.util.HashMap;
import java.util.Map;

public class MSG_CONST {
    public static final Map<String, String> MSG = new HashMap<>();
    static {
        // ALERT
        MSG.put("M001", "created");
        MSG.put("M002", "updated");
        MSG.put("M003", "required");
        MSG.put("M004", "already used");
        MSG.put("M005", "Invalid argument");

    }
}
