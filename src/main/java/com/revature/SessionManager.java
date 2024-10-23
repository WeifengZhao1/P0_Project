package com.revature;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static Map<String, Integer> userSessions = new HashMap<>(); // sessionId -> userId mapping

    public static void createSession(String sessionId, int userId) {
        userSessions.put(sessionId, userId);
    }

    public static Integer getUserIdFromSession(String sessionId) {
        return userSessions.get(sessionId);
    }
}
