package com.example.demo.test.socket;

import java.io.IOException;
import java.net.*;

public class StartUDPServer {

    public static void main(String[] args) {
        try {
            UDPSocketServer instance = UDPSocketServer.getInstance(12306, (short) 1024);
            instance.start(packet -> {
                int dataLen = packet.getLength();
                String data = new String(packet.getData(), 0, dataLen);
                InetAddress address = packet.getAddress();//获取发送端的ip
                String ip = address.getHostAddress();
                System.out.println("来自："+ip+"发送的数据是:"+data);
            });
            instance.close();
            System.out.println("end");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}




