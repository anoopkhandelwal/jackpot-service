package com.sportygroup.jackpot.util;

import java.util.UUID;

/**
 * Utility class for generating unique IDs.
 */
public class IdGenerator {

    /**
     * Generates a new unique ID using UUID.
     * @return A unique string ID.
     */
    public static String generateId() {
        return UUID.randomUUID().toString();
    }
}