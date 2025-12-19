package com.example.appcommons.Service;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.Security;
import java.util.Base64;

@Service

public class AESServiceImpl {
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/ECB/PKCS7Padding";
    private static final String CHARSET = "UTF-8";

    public static String encrypt(String input, String key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        Key aesKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encryptedBytes = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encryptedBytes);
    }

    public static String decrypt(String input, String key) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        Key aesKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(input));
        return new String(decryptedBytes, StandardCharsets.UTF_8);
    }

}
