package com.fs.everything.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    private static String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

    /**
     * 将字符串以MD5方式加密
     * @param str
     * @return
     */
    public static String encryptMD5(String str){
        if (null==str||"".equals(str)){
            return null;
        }
        MessageDigest md5 = null;
        byte[] bytes = null;
        try {
            md5 = MessageDigest.getInstance("MD5");
            bytes = md5.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return byteArrayToHexString(bytes);
    }


    /**
     * 将字符串以SHA1方式加密
     * @param str
     * @return
     */
    public static String encryptSHA1(String str){
        if (null==str||"".equals(str)){
            return null;
        }
        MessageDigest sha1 = null;
        byte[] bytes = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
            bytes = sha1.digest(str.getBytes("utf-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return byteArrayToHexString(bytes);
    }


    private static String byteArrayToHexString(byte[] b){
        StringBuffer resultSb = new StringBuffer();
        for(int i=0;i<b.length;i++){
            resultSb.append(byteToHexString(b[i]));
        }
        return resultSb.toString();
    }

    private static String byteToHexString(byte b){
        int n = b;
        if(n<0)
            n=256+n;
        int d1 = n/16;
        int d2 = n%16;
        return hexDigits[d1] + hexDigits[d2];
    }
}
