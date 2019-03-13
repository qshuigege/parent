package com.fs.everything.utils;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Base64;

public class AESUtils {

    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String generateKey() throws Exception{
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        kg.init(128);
        SecretKey secretKey = kg.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static String encrypt(String source, String key, String iv) throws Exception{
        IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(key), KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] bytes = cipher.doFinal(source.getBytes("utf-8"));
        return Base64.getEncoder().encodeToString(bytes);
    }

    public static String decrypt(String encryptedData, String key, String iv) throws Exception{
        IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(key), KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        return new String(bytes, "utf-8");
    }

}
