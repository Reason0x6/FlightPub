package com.FlightPub.Services;


import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Implements database interaction for account security
 */
public class SecurityService {

    private final MessageDigest digest;

    /**
     * Initialise the message digest algorithm
     */
    public SecurityService() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-256");
    }

    /**
     * Returns a SHA-256 hash of a plain text string
     *
     * @param text plain text to be hashed
     * @return String hashed plain text
     */
    public String hash(String text) {
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));

        return new String(Base64Utils.encode(hash));
    }
}
