package com.gpstracker.server.util;

import java.nio.ByteBuffer;
import java.util.UUID;

public class TokenGenerator {

    public static byte[] generateToken() {
        UUID uuid = UUID.randomUUID();
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

}
