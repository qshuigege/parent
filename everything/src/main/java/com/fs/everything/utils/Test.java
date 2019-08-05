package com.fs.everything.utils;

public class Test {

    public static void main(String[] args) throws Exception{
        String key = AESUtils.generateKey();
        String iv = AESUtils.generateKey();
        key = AESUtils.KEY;
        iv = AESUtils.IV_PARAM;
        System.out.println("key-->"+key+", iv-->"+iv);
        String pwd = "123456123456123456";
        System.out.println("pwd-->"+pwd);
        String result = AESUtils.encrypt(pwd, key, iv);
        System.out.println("encrypted pwd-->"+result+", length-->"+result.length());
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
