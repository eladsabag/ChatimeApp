package com.elad.chatimeapp.utils;

/**
 * @author - Elad Sabag
 * @date - 2/10/2023
 */

import android.util.Base64;
import com.elad.chatimeapp.BuildConfig;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class HashUtil {
    private static final String ENCRYPTION_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String CHARSET_NAME = StandardCharsets.UTF_8.name();

    public static String generateChatId(String phoneNumber1, String phoneNumber2) {
        String concatenated = phoneNumber1 + phoneNumber2;
        return hash(concatenated, BuildConfig.SECRET_KEY);
    }

    private static String hash(String input, String secretKey) {
        String concatenated = input + secretKey;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(concatenated.getBytes());
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

    public static String encrypt(String message, String secretKey) throws Exception {
        byte[] keyBytes = generateAESKey(secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        byte[] iv = new byte[cipher.getBlockSize()];
        new SecureRandom().nextBytes(iv);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] encrypted = cipher.doFinal(message.getBytes(CHARSET_NAME));
        byte[] ivAndEncrypted = new byte[iv.length + encrypted.length];
        System.arraycopy(iv, 0, ivAndEncrypted, 0, iv.length);
        System.arraycopy(encrypted, 0, ivAndEncrypted, iv.length, encrypted.length);
        return Base64.encodeToString(ivAndEncrypted, Base64.DEFAULT);
    }

    public static String decrypt(String encryptedMessage, String secretKey) throws Exception {
        byte[] ivAndEncrypted = Base64.decode(encryptedMessage, Base64.DEFAULT);
        byte[] iv = Arrays.copyOfRange(ivAndEncrypted, 0, 16);
        byte[] encrypted = Arrays.copyOfRange(ivAndEncrypted, 16, ivAndEncrypted.length);
        byte[] keyBytes = generateAESKey(secretKey);
        SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
        IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted, CHARSET_NAME);
    }

    private static byte[] generateAESKey(String secretKey) throws NoSuchAlgorithmException {
        MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        byte[] keyBytes = sha256.digest(secretKey.getBytes(StandardCharsets.UTF_8));
        return Arrays.copyOf(keyBytes, 32);
    }
}
