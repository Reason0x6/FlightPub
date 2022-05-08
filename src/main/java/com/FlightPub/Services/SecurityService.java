package com.FlightPub.Services;


import org.springframework.util.Base64Utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SecurityService {

    private MessageDigest digest ;

    public SecurityService() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-256");
    }

    public String hash(String text){
        byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));

        return new String(Base64Utils.encode(hash));
    }
}
