package com.example.demo.test.socket;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class TCPSocketServer2 {

    private ServerSocket serverSocket;
    private boolean isStarted = false;
    private boolean isClosed = false;

    private TCPSocketServer2(int port) throws IOException {
        this.serverSocket = new ServerSocket(port);
    }

    public static TCPSocketServer2 getInstance(int port) throws IOException {
        return new TCPSocketServer2(port);
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
                //socket.setSoTimeout(5000);
                InputStream in = socket.getInputStream();
                OutputStream out = socket.getOutputStream();

                byte[] inData = SocketUtils.getInputStreamData(in);

                socket.shutdownInput();


                out.write(inData.length);
                out.flush();

                socket.shutdownOutput();

                out.close();
                in.close();

                socket.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        TCPSocketServer2 instance = TCPSocketServer2.getInstance(1234);
        instance.start();
    }

}
