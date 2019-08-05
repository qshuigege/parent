package com.example.demo.test.socket;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

public class TCPSocketClient2 {

    private Socket clientSocket;
    //private boolean isClosed = false;

    public TCPSocketClient2(String serverIP, int serverPort) throws IOException {
        this.clientSocket = new Socket(serverIP, serverPort);
    }

    /*public static TCPSocketClient2 getInstance(String serverIP, int serverPort) throws IOException {
        return new TCPSocketClient2(serverIP, serverPort);
    }*/

    public byte[] send(byte[] data) throws IOException {

        OutputStream out = clientSocket.getOutputStream();
        InputStream in = clientSocket.getInputStream();

        //发送信息
        out.write(data);
        out.flush();
        clientSocket.shutdownOutput();

        //获取响应信息
        byte[] retVal = SocketUtils.getInputStreamData(in);
        //System.out.println();
        /*for (byte b : retVal) {
            System.out.println(Integer.toHexString(Byte.toUnsignedInt(b)));
        }*/

        clientSocket.shutdownInput();

        in.close();
        out.close();

        clientSocket.close();

        return retVal;
    }

    /*public void close(){
        if (isClosed){
            return;
        }
        isClosed = true;
        try {
            this.clientSocket.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/

    public static void main(String[] args) throws IOException {
        TCPSocketClient2 client = new TCPSocketClient2("192.168.53.211", 1234);
        byte[] data = new byte[]{0x44,0x52, 0x00,0x00, 0x2b, 0x03, 0x00,0x00, 0x00,0x03, 0x01,0x03,(byte)0xe5, (byte)0xb0};//0x03e6=998
        byte[] retVal = client.send(data);
        for (byte b : retVal) {
            System.out.println(Integer.toHexString(Byte.toUnsignedInt(b)));
        }
    }
}
