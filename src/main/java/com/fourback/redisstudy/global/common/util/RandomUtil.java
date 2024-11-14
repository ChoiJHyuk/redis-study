package com.fourback.redisstudy.global.common.util;

import java.security.SecureRandom;

public class RandomUtil {
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String genId() {
        byte[] randomBytes = new byte[3];
        SECURE_RANDOM.nextBytes(randomBytes);

        StringBuilder hexString = new StringBuilder();

        for (byte b : randomBytes) {
            hexString.append(String.format("%02x", b));
        }

        return hexString.toString();
    }
}
