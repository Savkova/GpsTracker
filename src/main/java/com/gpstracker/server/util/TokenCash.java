package com.gpstracker.server.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TokenCash {

    private static Map<UUID, Integer> TOKEN_CASH = new ConcurrentHashMap<>();

    public static Map<UUID, Integer> getTokens() {
        return TOKEN_CASH;
    }

}
