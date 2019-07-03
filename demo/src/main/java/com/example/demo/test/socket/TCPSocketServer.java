package com.example.demo.test.socket;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSocketServer {

    public static void main(String[] args) throws Exception{

        ServerSocket socket = new ServerSocket(2233);
        Socket accept = null;

        while (true) {
            accept = socket.accept();

            InputStream in = accept.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(in);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String temp = null;
            StringBuffer sb = new StringBuffer();
            while ((temp = reader.readLine()) != null) {
                sb.append(temp);
            }
            System.out.println(sb);
            accept.shutdownInput();

            OutputStream outputStream = accept.getOutputStream();
            outputStream.write(("客户端你好，你发送的内容为：" + sb.toString()).getBytes());
            outputStream.flush();
            accept.shutdownOutput();

            outputStream.close();

            reader.close();
            inputStreamReader.close();
            in.close();
            //accept.close();
            //socket.close();
        }

    }

}
