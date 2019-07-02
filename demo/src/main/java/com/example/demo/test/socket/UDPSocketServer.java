package com.example.demo.test.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPSocketServer{
    private DatagramSocket server;

    private UDPSocketServer(int port) throws SocketException {
        server = new DatagramSocket(port);
    }

    public static UDPSocketServer getInstance(int port) throws SocketException{
        return new UDPSocketServer(port);
    }

    public void start() throws SocketException, IOException {
        while (true){
            byte[] b = new byte[1024];
            DatagramPacket dp = new DatagramPacket(b, b.length);
            server.receive(dp);
            int dataLen = dp.getLength();
            String data = new String(dp.getData(), 0, dataLen);
            InetAddress address = dp.getAddress();//获取发送端的ip
            String ip = address.getHostAddress();
            System.out.println("来自："+ip+"发送的数据是:"+data);
            //5:关闭套接字
            //server.close();
        }
    }
}