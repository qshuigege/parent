package com.example.demo.test.socket;

import java.util.Scanner;

public class Test {

    public static void main(String[] args) {
        try {
            UDPSocketClient instance = UDPSocketClient.getInstanceReusable("192.168.50.232", 2000);
            //UDPSocketClient instance = UDPSocketClient.getInstanceReusable("localhost", 2300);


                byte t = 03;
            byte[] mess=new byte[3];
            mess[0]= (byte)1;
            mess[1]= (byte) 77 >> 8;
            mess[2]= (byte)77;
                UDPPacket udpPacket = new UDPPacket();
                byte[] sendData = udpPacket.getSendData(t, mess);
                //sendData = new byte[]{0x44, 0x52,0x00,0x00,0x2b,0x03,0x00,0x00,0x00,0x03,0x01,0x00,0x19,(byte)0xe1};
                instance.send(sendData);

                instance.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
