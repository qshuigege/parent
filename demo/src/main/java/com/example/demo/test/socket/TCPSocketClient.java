package com.example.demo.test.socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSocketClient {

    public static void main(String[] args) throws Exception{
        Socket socket = new Socket("localhost", 2233);
        //Socket socket = new Socket("192.168.50.232", 2400);

        OutputStream outputStream = socket.getOutputStream();
        //outputStream.write("我是Balla_兔子".getBytes());
        byte[] bytes = new byte[]{0x44, 0x52,0x00,0x00,0x2b,0x03,0x00,0x00,0x00,0x03,0x01,0x00,0x19,(byte)0xe1};
        outputStream.write(bytes);
        outputStream.flush();
        socket.shutdownOutput();
        //outputStream.close();

        InputStream in = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(in);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        String temp = null;
        StringBuffer sb = new StringBuffer();
        while ((temp = reader.readLine())!=null){
            sb.append(temp);
        }
        System.out.println(sb);

        socket.shutdownInput();
        reader.close();
        inputStreamReader.close();
        in.close();
        outputStream.close();
        socket.close();

        System.out.println("end...");

    }

}
