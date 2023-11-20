package com.child.utils;

import java.util.UUID;

public class StringUtil {

    /**
     * Generates a unique QR code string
     *
     * @return A unique QR code string.
     */
    public static String generateUniqueQRCode() {
        return UUID.randomUUID().toString();
    }
}

