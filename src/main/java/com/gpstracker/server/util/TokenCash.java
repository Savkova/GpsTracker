package com.gpstracker.server.util;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TokenCash {

    private static Map<Integer, UUID> TOKEN_CASH = new ConcurrentHashMap<>();

    public static Map<Integer, UUID> getTokens() {
        return TOKEN_CASH;
    }

}
