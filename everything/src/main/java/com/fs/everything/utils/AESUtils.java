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

    public static final String KEY = "FSxTB4UgbHp3sr0OMu2R7A==";
    public static final String IV_PARAM = "KwpfwiaP2srWzmkeMgcYYQ==";

    private static final String KEY_ALGORITHM = "AES";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS7Padding";



    //private static String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String generateKey() throws Exception {
        KeyGenerator kg = KeyGenerator.getInstance(KEY_ALGORITHM);
        kg.init(128);
        SecretKey secretKey = kg.generateKey();
        return Base64.getEncoder().encodeToString(secretKey.getEncoded());
    }

    public static String encrypt(String source, String key, String iv) throws Exception {
        IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(key), KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
        byte[] bytes = cipher.doFinal(source.getBytes("utf-8"));
        //return Base64.getEncoder().encodeToString(bytes);
        return byte2hex(bytes);
    }

    public static String decrypt(String encryptedData, String key, String iv) throws Exception {
        IvParameterSpec ivSpec = new IvParameterSpec(Base64.getDecoder().decode(iv));
        SecretKeySpec keySpec = new SecretKeySpec(Base64.getDecoder().decode(key), KEY_ALGORITHM);
        Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        //byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(encryptedData));
        byte[] bytes = cipher.doFinal(hex2byte(encryptedData.getBytes()));
        return new String(bytes, "utf-8");
    }

    /*private static String byteArrayToHexString(byte[] b){
        StringBuffer resultSb = new StringBuffer();
        for(int i=0;i<b.length;i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }*/

    /*private static String byteToHexString(byte b){
        int n = b;
        if(n<0)
            n=256+n;
        int d1 = n/16;
        int d2 = n%16;
        return hexDigits[d1] + hexDigits[d2];
    }*/

    private static byte[] hex2byte(byte[] b) {
        if ((b.length % 2) != 0)
            throw new IllegalArgumentException("长度不是偶数");
        byte[] b2 = new byte[b.length / 2];
        for (int n = 0; n < b.length; n += 2) {
            String item = new String(b, n, 2);
            b2[n / 2] = (byte) Integer.parseInt(item, 16);
        }
        return b2;
    }

    private static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs;
    }

}
