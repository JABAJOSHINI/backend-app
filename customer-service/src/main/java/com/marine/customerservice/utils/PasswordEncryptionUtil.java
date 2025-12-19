package com.marine.customerservice.utils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Base64;
@Component
public class PasswordEncryptionUtil {
//    private static String SECRET_KEY;
//
//    @Value("${SECRET_KEY}")
//    private String secretKeyTemp;
//
//    @PostConstruct
//    public void init() {
//        SECRET_KEY = secretKeyTemp;
//    }
//    private static byte[] getAESKey() throws Exception {
//        MessageDigest sha = MessageDigest.getInstance("SHA-256");
//        byte[] key = sha.digest(SECRET_KEY.getBytes("UTF-8"));
//        return key; // Ensures exactly 32 bytes (256 bits)
//    }
//
//    public static String decrypt(String encryptedText) throws Exception {
//        byte[] key = getAESKey();
//        SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
//
//        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
//        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
//
//        return new String(decryptedBytes, "UTF-8"); // Ensure UTF-8 encoding
//    }


//    public static String decrypt(String encryptedPassword) {
//        try {
//            SecretKey key = new SecretKeySpec(SECRET_KEY.getBytes(), "AES");
//            Cipher cipher = Cipher.getInstance("AES");
//            cipher.init(Cipher.DECRYPT_MODE, key);
//            byte[] decodedBytes = Base64.getDecoder().decode(encryptedPassword);
//            return new String(cipher.doFinal(decodedBytes));
//        } catch (Exception e) {
//            throw new RuntimeException("Error decrypting password", e);
//        }
//    }
}
