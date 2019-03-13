package com.fs.everything.utils;

public class Test {

    public static void main(String[] args) throws Exception{
        String key = AESUtils.generateKey();
        String iv = AESUtils.generateKey();
        String result = AESUtils.encrypt("富森供应链管理有限公司", key, iv);
        System.out.println(result);
        String decrypt = AESUtils.decrypt(result, key, iv);
        System.out.println(decrypt);

        String str = "111,222,333,";
        String[] split = str.split(",");
        System.out.println(split.length);
        for (int i = 0; i < split.length; i++) {
            System.out.println(split[i]);
        }


        System.out.println(System.currentTimeMillis());
    }
}
