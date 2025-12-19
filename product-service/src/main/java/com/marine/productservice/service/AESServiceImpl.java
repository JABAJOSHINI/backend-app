
package com.marine.productservice.service;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.stereotype.Service;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

@Service

public class AESServiceImpl {

    private static SecretKeySpec secretKey;
    private static byte[] key;

    public static void setKey(final String myKey) {
        MessageDigest sha = null;
        try {
            key = myKey.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static String encrypt(final String strToEncrypt, final String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            return Base64.getEncoder()
                    .encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8")));

        } catch (Exception e) {
            System.out.println("Error while encrypting: " + e.toString());
        }
        return null;
    }

    public static String decrypt(final String strToDecrypt, final String secret) {
        try {
            setKey(secret);
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            return new String(cipher.doFinal(Base64.getDecoder()
                    .decode(strToDecrypt)));
        } catch (Exception e) {
            System.out.println("Error while decrypting: " + e.toString());
        }
        return null;
    }


    /*private static final String ALGORITHM = "AES";
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
    }*/

}
