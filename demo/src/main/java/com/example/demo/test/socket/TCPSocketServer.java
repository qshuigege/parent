package com.example.demo.test.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPSocketServer {

    private ServerSocket serverSocket;
    private boolean isStarted = false;
    private boolean isClosed = false;

    private TCPSocketServer(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public static TCPSocketServer getInstance(int port) throws IOException {
        return new TCPSocketServer(port);
    }

    public void close(){
        if (isClosed){
            return;
        }
        isClosed = true;
        try {
            serverSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void start() throws IOException {
        if (isStarted){
            return;
        }
        isStarted = true;
        while (true){

            Socket acceptSocket = serverSocket.accept();
            new SocketHandleThread(acceptSocket).start();

        }
    }

    private class SocketHandleThread extends Thread{
        private Socket socket;
        public SocketHandleThread(Socket socket){
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
                InputStream in = socket.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(in);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String temp = null;
                StringBuffer sb = new StringBuffer();
                while ((temp = reader.readLine()) != null) {
                    sb.append(temp);
                    System.out.println(temp);
                }
                System.out.println("接收到消息：" + sb);
                socket.shutdownInput();

                OutputStream outputStream = socket.getOutputStream();
                outputStream.write(("客户端你好，你发送的内容为：" + sb.toString()).getBytes());
                outputStream.flush();
                socket.shutdownOutput();

                outputStream.close();

                reader.close();
                inputStreamReader.close();
                in.close();
                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
