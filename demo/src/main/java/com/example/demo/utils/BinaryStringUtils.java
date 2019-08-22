package com.example.demo.utils;

public class BinaryStringUtils {

    public static String byte2hex(byte[] b) {
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

    public static String byte2hexAndPretty(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs.append("0x0").append(stmp).append(", ");
            }
            else {
                hs.append("0x").append(stmp).append(", ");
            }
        }
        if (hs.length()>2) {
            hs.setLength(hs.length() - 2);
        }
        return hs.toString();
    }

    public static String hexPretty(String hexString) {
        StringBuilder hs = new StringBuilder();
        char[] chars = hexString.toCharArray();
        for (int i = 1; i < chars.length+1; i++) {
            if (i%2 == 0) {
                hs.append("0x").append(chars[i-2]).append(chars[i-1]).append(", ");
            }
        }
        if (hs.length()>2) {
            hs.setLength(hs.length() - 2);
        }
        return hs.toString();
    }


}
