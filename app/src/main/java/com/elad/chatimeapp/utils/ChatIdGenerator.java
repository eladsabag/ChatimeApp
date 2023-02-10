package com.elad.chatimeapp.utils;

/**
 * @author - Elad Sabag
 * @date - 2/10/2023
 */
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChatIdGenerator {

    public static String generateChatId(String phoneNumber1, String phoneNumber2) {
        String concatenated = phoneNumber1 + phoneNumber2;
        return hash(concatenated);
    }

    private static String hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            StringBuilder hashText = new StringBuilder(no.toString(16));
            while (hashText.length() < 32) {
                hashText.insert(0, "0");
            }
            return hashText.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
