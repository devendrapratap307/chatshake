package com.chatshake.chat.chatshake_app.config;

import com.chatshake.chat.chatshake_app.dto.UserTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserContext {
    private static final ThreadLocal<UserTO> currentUser = new ThreadLocal<>();
    public static void setCurrentUser(UserTO userTO) {
        log.debug("User Context Session =============================== " + userTO);
        currentUser.set(userTO);
    }

    public static UserTO getCurrentUser() {
        return currentUser.get();
    }
    public static void clearCurrentUser() {
        currentUser.remove();
    }
}
