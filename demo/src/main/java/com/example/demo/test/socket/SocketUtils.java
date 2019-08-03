package com.example.demo.test.socket;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class SocketUtils {

    public static byte[] getInputStreamData(InputStream in) throws IOException {
        List<Object> content = new ArrayList<>();
        byte[] buff = new byte[10];
        int totalReadBytes = 0;
        int curReadBytes = 0;
        while (true) {
            try {
                curReadBytes = in.read(buff);
                if (curReadBytes == -1) {
                    System.out.println("对方输出流已关闭！");
                    break;
                }
            }catch (SocketTimeoutException e){
                System.out.println("读取输入流超时！");
                break;
            }

            System.out.println("本次读到的长度-->"+curReadBytes);
            totalReadBytes += curReadBytes;
            byte[] temp = new byte[curReadBytes];
            System.arraycopy(buff, 0, temp, 0, curReadBytes);
            content.add(temp);

        }
        System.out.println("总长度-->"+totalReadBytes);

        byte[] retVal = new byte[totalReadBytes];

        for (int i = 0; i < content.size(); i++) {
            byte[] t = (byte[])content.get(i);
            System.arraycopy(t, 0, retVal, buff.length*i, t.length);
        }
        return retVal;
    }
}
