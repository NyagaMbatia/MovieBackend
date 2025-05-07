package com.joe.auth.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class AuthConstants {

    public static final String SECRET_KEY = keyGen();
    public static final long JWT_EXPIRATION_TIME = 20 * 10000;
            // 86400000;
    public static final long JWT_REFRESH_EXPIRATION_TIME = 30 * 1000;
                    // 604800000;

    private static String keyGen() {
        // Generate a secure random key of 64 bytes (512 bits)
        SecureRandom secureRandom = new SecureRandom();
        byte[] key = new byte[64];
        secureRandom.nextBytes(key);

        // Encode the key using Base64 for string representation
        return Base64.getEncoder().encodeToString(key);
    }
}
